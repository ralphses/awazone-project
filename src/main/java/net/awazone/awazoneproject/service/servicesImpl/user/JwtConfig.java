package net.awazone.awazoneproject.service.servicesImpl.user;

import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.awazone.awazoneproject.model.userService.JwtToken;
import net.awazone.awazoneproject.repository.user.AwazoneUserRepository;
import net.awazone.awazoneproject.repository.user.JwtTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static com.google.common.net.HttpHeaders.REFRESH;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {
    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    @Autowired
    private AwazoneUserRepository awazoneUserRepository;

    private String secreteKey;
    private String accessTokenPrefix;
    public static String ACCESS_TOKEN_PREFIX = AUTHORIZATION;
    public static String REFRESH_TOKEN_PREFIX = REFRESH;
    private String refreshTokenPrefix;
    private Integer tokenExpirationAfter;

    public String getAuthorizationHeader() {
        return AUTHORIZATION;
    }

    public String getRefreshHeader() {
        return REFRESH;
    }

    public String createAccessToken(String subject, Collection<? extends GrantedAuthority> authorities, SecretKey secretKey, HttpServletRequest httpServletRequest) {

        String token = Jwts.builder()
                .setSubject(subject)
                .claim("authorities", authorities)
                .claim("origin", httpServletRequest.getRemoteAddr())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                .signWith(secretKey)
                .compact();

        jwtTokenRepository.save(createNewJwtToken(subject, token, httpServletRequest, AUTHORIZATION));
        return token;
    }

    private JwtToken createNewJwtToken(String subject, String token, HttpServletRequest httpServletRequest, String type) {
        return JwtToken.builder()
                .jwtTokenString(token)
                .awazoneUser(awazoneUserRepository.findByAwazoneUserContactEmail(subject).get())
                .type(type)
                .valid(true)
                .remoteAddress(httpServletRequest.getRemoteAddr())
                .loggedIn(true)
                .loggedInAt(LocalDateTime.now())
                .build();
    }

    public String createRefreshToken(String subject, Collection<? extends GrantedAuthority> authorities, SecretKey secretKey, HttpServletRequest httpServletRequest) {

        String refreshToken = Jwts.builder()
                .setSubject(subject)

                .claim("origin", httpServletRequest.getRemoteAddr())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(3)))
                .signWith(secretKey)
                .compact();

        jwtTokenRepository.save(createNewJwtToken(subject, refreshToken, httpServletRequest, REFRESH));
        return refreshToken;
    }

    public void createNewTokens(String refreshToken) {
    }
}
