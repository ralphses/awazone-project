package net.awazone.awazoneproject.configuration.properties;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertyConfig {

    @Value("${property.someThingGood}")
    private String somethingGood;

    @Value("${property.algorithm}")
    private String algorithm;


    @Bean(name = "jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor pooledPBEStringEncryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig simpleStringPBEConfig = new SimpleStringPBEConfig();
        simpleStringPBEConfig.setPassword(somethingGood);
        simpleStringPBEConfig.setAlgorithm(algorithm);
        simpleStringPBEConfig.setKeyObtentionIterations("1000");
        simpleStringPBEConfig.setPoolSize("1");
        simpleStringPBEConfig.setProviderName("SunJCE");
        simpleStringPBEConfig.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        simpleStringPBEConfig.setStringOutputType("base64");

        pooledPBEStringEncryptor.setConfig(simpleStringPBEConfig);

        return pooledPBEStringEncryptor;
    }

}
