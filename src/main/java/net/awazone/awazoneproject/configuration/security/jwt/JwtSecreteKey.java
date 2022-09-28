package net.awazone.awazoneproject.configuration.security.jwt;

import io.jsonwebtoken.security.Keys;
import net.awazone.awazoneproject.service.servicesImpl.user.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtSecreteKey {

    private final JwtConfig jwtConfig;

    @Autowired
    public JwtSecreteKey(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Bean
    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecreteKey().getBytes());
    }
}
