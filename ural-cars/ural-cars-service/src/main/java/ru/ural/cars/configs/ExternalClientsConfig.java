package ru.ural.cars.configs;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import ru.ural.cars.configs.properties.AutocodeProperties;
import ru.ural.cars.configs.properties.CarPhotoAnalysisKafkaProperties;

@Configuration
@EnableConfigurationProperties({
        CarPhotoAnalysisKafkaProperties.class,
        AutocodeProperties.class
})
public class ExternalClientsConfig {

    @Bean
    public RestClient autocodeRestClient(RestClient.Builder builder, AutocodeProperties properties) {
        return builder
                .baseUrl(properties.getBaseUrl())
                .build();
    }

}
