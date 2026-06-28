package ru.ural.contracts.senders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.ural.contracts.properties.KafkaProperty;
import ru.ural.contracts.services.ProducerService;
import ru.ural.notifications.dto.contract.NotificationContractRequest;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationSender {

    private final KafkaProperty kafkaProperty;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ProducerService producerService;

    public void sendContractNotification(NotificationContractRequest request) {
        String body = producerService.objectToString(request);

        KafkaProperty.Topic topic = kafkaProperty.getNotificationContractTopic();

        String correlationId = UUID.randomUUID().toString();

        try {
            kafkaTemplate.send(topic.getName(), correlationId, body)
                    .whenComplete((result, exception) -> {
                        if (exception != null) {
                            log.error("Failed to send contract notification to topic {}", topic.getName(), exception);
                        }
                    });
        } catch (KafkaException exception) {
            log.error("Failed to send contract notification to topic {}", topic.getName(), exception);
        }
    }

}
