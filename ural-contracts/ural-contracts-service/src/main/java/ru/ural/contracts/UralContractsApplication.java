package ru.ural.contracts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class UralContractsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UralContractsApplication.class, args);
    }

}
