package ru.ural.ai.configs.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "integrations.gemini")
public class GeminiProperties {

    private String baseUrl = "https://generativelanguage.googleapis.com";

    private String apiKey = "";

    private String model = "gemini-2.5-flash";

}
