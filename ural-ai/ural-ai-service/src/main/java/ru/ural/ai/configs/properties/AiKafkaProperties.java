package ru.ural.ai.configs.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.kafka.ai")
public class AiKafkaProperties {

    private CarPhotoAnalysis carPhotoAnalysis = new CarPhotoAnalysis();

    @Getter
    @Setter
    public static class CarPhotoAnalysis {

        private String requestTopic = "ural-ai.car-photo-analysis.requested";

        private String resultTopic = "ural-ai.car-photo-analysis.completed";

    }

}
