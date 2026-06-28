package ru.ural.notifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class UralNotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(UralNotificationApplication.class, args);
    }

}
