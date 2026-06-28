package ru.ural.users.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.ural.users.dto.UserRatingUpdatedEvent;
import ru.ural.users.services.UserRatingService;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRatingKafkaListener {

    private final ObjectMapper objectMapper;

    private final UserRatingService userRatingService;

    @KafkaListener(
            topics = "${kafka.user-rating-topic.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(String payload) {
        try {
            UserRatingUpdatedEvent event = objectMapper.readValue(payload, UserRatingUpdatedEvent.class);
            userRatingService.updateRating(event);
        } catch (Exception exception) {
            log.error("Failed to process user rating event: {}", payload, exception);
        }
    }

}
