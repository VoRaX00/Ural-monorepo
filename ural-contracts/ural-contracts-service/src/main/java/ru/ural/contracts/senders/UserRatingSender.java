package ru.ural.contracts.senders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.ural.contracts.events.UserRatingUpdatedEvent;
import ru.ural.contracts.properties.KafkaProperty;
import ru.ural.contracts.services.ProducerService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRatingSender {

    private final KafkaProperty kafkaProperty;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ProducerService producerService;

    public void sendUserRatingUpdated(UserRatingUpdatedEvent event) {
        String body = producerService.objectToString(event);
        KafkaProperty.Topic topic = kafkaProperty.getUserRatingTopic();
        String correlationId = UUID.randomUUID().toString();

        try {
            kafkaTemplate.send(topic.getName(), correlationId, body)
                    .whenComplete((result, exception) -> {
                        if (exception != null) {
                            log.error("Failed to send user rating event to topic {}", topic.getName(), exception);
                        }
                    });
        } catch (KafkaException exception) {
            log.error("Failed to send user rating event to topic {}", topic.getName(), exception);
        }
    }

}
