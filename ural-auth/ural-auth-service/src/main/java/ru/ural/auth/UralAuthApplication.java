package ru.ural.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class UralAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(UralAuthApplication.class, args);
    }

}
