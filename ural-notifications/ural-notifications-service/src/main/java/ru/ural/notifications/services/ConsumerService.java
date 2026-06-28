package ru.ural.notifications.services;

import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ural.exceptions.InternalServerException;
import ru.ural.notifications.dto.email.EmailNotificationRequest;
import ru.ural.notifications.dto.contract.NotificationContractRequest;

@Service
@RequiredArgsConstructor
public class ConsumerService {

    private final JsonMapper jsonMapper;

    public EmailNotificationRequest convertNotificationRequest(String message) {
        return convertRequest(message, EmailNotificationRequest.class);
    }

    public NotificationContractRequest convertNotificationContractRequest(String message) {
        return convertRequest(message, NotificationContractRequest.class);
    }

    private <T> T convertRequest(String message, Class<T> clazz) {
        try {
            return jsonMapper.readValue(message, clazz);
        } catch (Exception e) {
            throw new InternalServerException(String.format("Invalid mapping request with message: %s", message), e);
        }
    }

}
