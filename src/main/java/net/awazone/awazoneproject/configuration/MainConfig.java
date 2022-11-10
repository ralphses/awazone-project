package net.awazone.awazoneproject.configuration;

import net.awazone.awazoneproject.service.servicesImpl.user.JwtConfig;
import net.awazone.awazoneproject.utility.Utility;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Arrays.stream;

@Configuration
public class MainConfig {

    public static final String PAYMENT_REDIRECT_URL = "/aibopay/transaction/confirm";

    @Bean
    Utility utility() {
        return new Utility();
    }

    @Bean
    JwtConfig jwtConfig() {
        return new JwtConfig();
    }

}
