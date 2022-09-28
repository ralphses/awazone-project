package net.awazone.awazoneproject;

import net.awazone.awazoneproject.utility.files.FileStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileStore.class})
public class AwazoneProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(AwazoneProjectApplication.class, args);
    }

}
