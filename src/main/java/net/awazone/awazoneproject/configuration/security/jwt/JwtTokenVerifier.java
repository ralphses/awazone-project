package net.awazone.awazoneproject.configuration.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import net.awazone.awazoneproject.controller.exception.IllegalUserException;
import net.awazone.awazoneproject.controller.exception.InvalidTokenException;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUser;
import net.awazone.awazoneproject.repository.user.JwtTokenRepository;
import net.awazone.awazoneproject.service.servicesImpl.user.AuthServiceImpl;
import net.awazone.awazoneproject.service.servicesImpl.user.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.net.HttpHeaders.REFRESH;
import static net.awazone.awazoneproject.controller.exception.MainException.INVALID_TOKEN_MESSAGE;
import static net.awazone.awazoneproject.controller.exception.ResponseMessage.ILLEGAL_USER;

@Slf4j
public class JwtTokenVerifier extends OncePerRequestFilter {

    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    public JwtTokenVerifier(SecretKey secretKey, JwtConfig jwtConfig) {
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Optional<String> authorizationHeader = Optional.ofNullable(request.getHeader(jwtConfig.getAuthorizationHeader()));

        if(request.getServletPath().equals("/user/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        if(request.getServletPath().equals("/user/reactivate-link")) {
            filterChain.doFilter(request, response);
            return;
        }

        if(request.getServletPath().equals("/user/refresh")) {
            try {
                //Todo: GEt token
                String refreshToken = Optional.ofNullable(request.getHeader(REFRESH))
                        .orElseThrow(() -> new IllegalUserException(ILLEGAL_USER)).substring(jwtConfig.getRefreshTokenPrefix().length());

                //TODO: Verify token
                Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(refreshToken);
                String username = claimsJws.getBody().getSubject();

                //TODO: verify that user is not logged out
                if(!AuthServiceImpl.authService.userLoggedIn(username)) {
                    throw new IllegalUserException(ILLEGAL_USER);
                }

                //TODO: Generate new token
                AwazoneUser awazoneUser = AuthServiceImpl.authService.findByEmail(username);
                Set<SimpleGrantedAuthority> auth = awazoneUser.getAuthorities().stream().map(aut -> new SimpleGrantedAuthority(aut.getAuthority())).collect(Collectors.toSet());

                String accessToken = jwtConfig.createAccessToken(username, auth, secretKey, request);
                response.setHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getAccessTokenPrefix() + accessToken);


            } catch (JwtException jwtException) {
                throw new InvalidTokenException(INVALID_TOKEN_MESSAGE);
            }

            filterChain.doFilter(request, response);
        }

        if(authorizationHeader.isPresent() && authorizationHeader.get().startsWith("Bearer_")) {
            try {
                String token = authorizationHeader.orElseThrow(() -> new InvalidTokenException(INVALID_TOKEN_MESSAGE)).replace(jwtConfig.getAccessTokenPrefix(), "");
                Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);

                Claims claims = claimsJws.getBody();
                String username = claims.getSubject();

                if(!AuthServiceImpl.authService.userLoggedIn(username)) {
                    throw new IllegalUserException(ILLEGAL_USER);
                }

                var authorities = (List<Map<String, String>>) claims.get("authorities");

                Set<SimpleGrantedAuthority> userAuthorities = authorities.stream().map(m -> new SimpleGrantedAuthority(m.get("authority"))).collect(Collectors.toSet());
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, userAuthorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            catch (JwtException jwtException) {
                throw new InvalidTokenException("Bad Token", jwtException);
            }
        }
        else {
            filterChain.doFilter(request, response);
            return;
        }


        filterChain.doFilter(request, response);
    }
}
