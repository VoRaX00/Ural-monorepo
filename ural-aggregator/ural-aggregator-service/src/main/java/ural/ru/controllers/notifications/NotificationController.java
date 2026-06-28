package ural.ru.controllers.notifications;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ural.ru.controllers.AbstractCommonController;
import ural.ru.services.proxy.ProxyService;

import java.util.Map;

@RestController
@RequestMapping("/notifications")
@Tag(name = "Контроллер для управления уведомлениями")
public class NotificationController extends AbstractCommonController {

    protected NotificationController(Map<String, ProxyService> proxyServiceMap) {
        super(proxyServiceMap);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping
    ResponseEntity<?> getNotifications(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PatchMapping("/{id}")
    ResponseEntity<?> markAsRead(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @Override
    protected String getProxyServiceName() {
        return "notificationProxyService";
    }
}
