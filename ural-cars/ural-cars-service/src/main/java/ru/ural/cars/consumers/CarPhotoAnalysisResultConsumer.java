package ru.ural.cars.consumers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.ural.ai.events.CarPhotoAnalysisCompletedEvent;
import ru.ural.cars.enums.CarConditionStatus;
import ru.ural.cars.repositories.CarRepository;
import ru.ural.exceptions.NotFoundException;

import java.time.ZonedDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class CarPhotoAnalysisResultConsumer {

    private final CarRepository carRepository;

    @KafkaListener(
            topics = "${app.kafka.car-photo-analysis.result-topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(CarPhotoAnalysisCompletedEvent event) {
        log.info("Received car photo analysis result for car id: {}", event.getCarId());

        var car = carRepository.findById(event.getCarId())
                .orElseThrow(() -> new NotFoundException("Not found car by id: %s".formatted(event.getCarId())));

        car.setPhotoAnalysisSummary(event.getSummary());
        car.setPhotoAnalysisStatus(parseStatus(event.getStatus()));
        car.setUpdatedAt(ZonedDateTime.now());
        carRepository.save(car);
    }

    private CarConditionStatus parseStatus(String value) {
        try {
            return CarConditionStatus.valueOf(value);
        } catch (RuntimeException exception) {
            return CarConditionStatus.UNKNOWN;
        }
    }

}
