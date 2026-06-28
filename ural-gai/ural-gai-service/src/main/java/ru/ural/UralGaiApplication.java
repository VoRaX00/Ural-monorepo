package ru.ural;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class UralGaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UralGaiApplication.class, args);
    }

}
