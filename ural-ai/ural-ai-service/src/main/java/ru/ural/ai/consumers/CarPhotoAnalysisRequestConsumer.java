package ru.ural.ai.consumers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.ural.ai.events.CarPhotoAnalysisRequestedEvent;
import ru.ural.ai.services.CarPhotoAnalysisService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CarPhotoAnalysisRequestConsumer {

    private final CarPhotoAnalysisService carPhotoAnalysisService;

    @KafkaListener(
            topics = "${app.kafka.ai.car-photo-analysis.request-topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(CarPhotoAnalysisRequestedEvent event) {
        log.info("Received car photo analysis request for car id: {}", event.getCarId());
        carPhotoAnalysisService.analyze(event);
    }

}
