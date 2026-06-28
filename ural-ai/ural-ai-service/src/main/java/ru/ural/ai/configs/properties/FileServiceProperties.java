package ru.ural.ai.configs.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "integrations.files")
public class FileServiceProperties {

    private String baseUrl;

}
