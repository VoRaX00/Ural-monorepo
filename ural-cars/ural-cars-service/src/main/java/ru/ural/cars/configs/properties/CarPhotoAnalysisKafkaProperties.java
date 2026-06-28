package ru.ural.cars.configs.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.kafka.car-photo-analysis")
public class CarPhotoAnalysisKafkaProperties {

    private String requestTopic = "ural-ai.car-photo-analysis.requested";

    private String resultTopic = "ural-ai.car-photo-analysis.completed";

}
