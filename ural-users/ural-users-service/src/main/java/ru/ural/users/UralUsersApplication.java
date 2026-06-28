package ru.ural.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class UralUsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(UralUsersApplication.class, args);
    }

}
