package ru.ural.ai.configs;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import ru.ural.ai.configs.properties.AiKafkaProperties;
import ru.ural.ai.configs.properties.CargoServiceProperties;
import ru.ural.ai.configs.properties.CarsServiceProperties;
import ru.ural.ai.configs.properties.FileServiceProperties;
import ru.ural.ai.configs.properties.GeminiProperties;

@Configuration
@EnableConfigurationProperties({
        AiKafkaProperties.class,
        CargoServiceProperties.class,
        CarsServiceProperties.class,
        FileServiceProperties.class,
        GeminiProperties.class
})
public class AiServiceConfig {

    @Bean
    public RestClient fileServiceRestClient(
            RestClient.Builder builder,
            FileServiceProperties properties
    ) {
        return builder
                .baseUrl(properties.getBaseUrl())
                .build();
    }

    @Bean
    public RestClient cargoServiceRestClient(
            RestClient.Builder builder,
            CargoServiceProperties properties
    ) {
        return builder
                .baseUrl(properties.getBaseUrl())
                .build();
    }

    @Bean
    public RestClient carsServiceRestClient(
            RestClient.Builder builder,
            CarsServiceProperties properties
    ) {
        return builder
                .baseUrl(properties.getBaseUrl())
                .build();
    }

    @Bean
    public RestClient geminiRestClient(
            RestClient.Builder builder,
            GeminiProperties properties
    ) {
        return builder
                .baseUrl(properties.getBaseUrl())
                .defaultHeader("x-goog-api-key", properties.getApiKey())
                .build();
    }

    @Bean
    public RestClient imageDownloadRestClient(RestClient.Builder builder) {
        return builder.build();
    }

}
