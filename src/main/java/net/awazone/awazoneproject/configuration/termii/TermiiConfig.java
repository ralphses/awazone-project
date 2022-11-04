package net.awazone.awazoneproject.configuration.termii;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TermiiConfig {

    public static final String  BASE_URL = "https://api.ng.termii.com";
    public static final String  SEND_SMS_URI = "/api/sms/send";

    @Bean
    TermiiCredential termiiCredential() {
        return new TermiiCredential();
    }
}
