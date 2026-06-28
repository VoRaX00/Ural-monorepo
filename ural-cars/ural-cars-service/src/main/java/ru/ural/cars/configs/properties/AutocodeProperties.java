package ru.ural.cars.configs.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.autocode")
public class AutocodeProperties {

    private String baseUrl;

}
