package ru.ural.notifications.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.ural.notifications.api.NotificationApi;
import ru.ural.notifications.dto.contract.NotificationContractDto;
import ru.ural.notifications.services.NotificationService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NotificationController implements NotificationApi {

    private final NotificationService notificationService;

    @Override
    public ResponseEntity<List<NotificationContractDto>> getNotifications() {
        return ResponseEntity.ok(notificationService.getNotifications());
    }

    @Override
    public ResponseEntity<Void> markAsRead(Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

}
