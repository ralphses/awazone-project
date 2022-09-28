package net.awazone.awazoneproject.configuration.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.awazone.awazoneproject.controller.exception.UserNotActiveException;
import net.awazone.awazoneproject.model.requests.user.UsernameAndPasswordAuthenticationRequest;
import net.awazone.awazoneproject.service.servicesImpl.user.AuthServiceImpl;
import net.awazone.awazoneproject.service.servicesImpl.user.JwtConfig;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static net.awazone.awazoneproject.controller.exception.ResponseMessage.USER_NOT_ACTIVATED;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager, SecretKey secretKey, JwtConfig jwtConfig) {
        this.authenticationManager = authenticationManager;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
        setFilterProcessesUrl("/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)  {

        try {
            UsernameAndPasswordAuthenticationRequest usernameAndPasswordAuthenticationRequest = new ObjectMapper().readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);

            if(!AuthServiceImpl.authService.isUserActivated(usernameAndPasswordAuthenticationRequest.getUsername())) {
                throw new UserNotActiveException(USER_NOT_ACTIVATED);
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(usernameAndPasswordAuthenticationRequest.getUsername(), usernameAndPasswordAuthenticationRequest.getPassword());
            return authenticationManager.authenticate(authentication);

        } catch (IOException | AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String accessToken = jwtConfig.createAccessToken(authResult.getName(), authResult.getAuthorities(), secretKey, request);
        String refreshToken = jwtConfig.createRefreshToken(authResult.getName(), authResult.getAuthorities(), secretKey, request);

        response.setHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getAccessTokenPrefix() + accessToken);
        response.setHeader(jwtConfig.getRefreshHeader(), jwtConfig.getRefreshTokenPrefix() + refreshToken);
    }

}
