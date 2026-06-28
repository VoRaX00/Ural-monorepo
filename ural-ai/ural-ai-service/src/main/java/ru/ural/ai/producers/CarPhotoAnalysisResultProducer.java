package ru.ural.ai.producers;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.ural.ai.configs.properties.AiKafkaProperties;
import ru.ural.ai.events.CarPhotoAnalysisCompletedEvent;

@Component
@RequiredArgsConstructor
public class CarPhotoAnalysisResultProducer {

    private final KafkaTemplate<String, CarPhotoAnalysisCompletedEvent> kafkaTemplate;

    private final AiKafkaProperties properties;

    public void send(CarPhotoAnalysisCompletedEvent event) {
        kafkaTemplate.send(
                properties.getCarPhotoAnalysis().getResultTopic(),
                event.getCarId().toString(),
                event
        );
    }

}
