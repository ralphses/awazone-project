package net.awazone.awazoneproject.configuration.coinremmita;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Base64;

public class CoinRemmitaConfig {

    private String password;

    public static final String TCN_API_KEY = "$2y$10$BZUXypYCHfLlAQAEp5uq3uZ4byraajrVh6LnsEuZ8ILa5LWwNXIcq";

    public CoinRemmitaConfig() {
        this.password = new String(Base64.getDecoder().decode(this.password));
        System.out.println("password = " + password);
    }

//    public static final String password;
}
