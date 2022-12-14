package net.awazone.awazoneproject;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import net.awazone.awazoneproject.configuration.monnify.MonifyCredentials;
import net.awazone.awazoneproject.utility.files.FileStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@OpenAPIDefinition
@SpringBootApplication
@EnableEncryptableProperties
@EnableConfigurationProperties(
        {
                FileStore.class,
                MonifyCredentials.class
        })
public class AwazoneProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(AwazoneProjectApplication.class, args);
    }

}
