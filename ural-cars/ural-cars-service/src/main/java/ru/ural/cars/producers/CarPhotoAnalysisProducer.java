package ru.ural.cars.producers;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.ural.ai.events.CarPhotoAnalysisRequestedEvent;
import ru.ural.cars.configs.properties.CarPhotoAnalysisKafkaProperties;

@Component
@RequiredArgsConstructor
public class CarPhotoAnalysisProducer {

    private final KafkaTemplate<String, CarPhotoAnalysisRequestedEvent> kafkaTemplate;

    private final CarPhotoAnalysisKafkaProperties properties;

    public void send(CarPhotoAnalysisRequestedEvent event) {
        kafkaTemplate.send(properties.getRequestTopic(), event.getCarId().toString(), event);
    }

}
