package ru.ural.ai.configs.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "integrations.cars")
public class CarsServiceProperties {

    private String baseUrl;

}
