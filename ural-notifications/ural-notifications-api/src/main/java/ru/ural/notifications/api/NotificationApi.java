package ru.ural.notifications.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ural.notifications.dto.contract.NotificationContractDto;

import java.util.List;

@RequestMapping("/api/notifications")
@Tag(name = "Контроллер для работы с уведомлениями")
public interface NotificationApi {

    @GetMapping
    ResponseEntity<List<NotificationContractDto>> getNotifications();

    @PatchMapping("/{id}")
    ResponseEntity<Void> markAsRead(@PathVariable Long id);

}
