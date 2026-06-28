package ru.ural.notifications.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ural.exceptions.NotFoundException;
import ru.ural.models.UserPrincipals;
import ru.ural.notifications.dto.contract.NotificationContractDto;
import ru.ural.notifications.dto.email.EmailNotificationRequest;
import ru.ural.notifications.dto.contract.NotificationContractRequest;
import ru.ural.notifications.entities.ContractNotification;
import ru.ural.notifications.entities.EmailNotification;
import ru.ural.notifications.mappers.NotificationMapper;
import ru.ural.notifications.repositories.ContractNotificationRepository;
import ru.ural.notifications.repositories.EmailNotificationRepository;
import ru.ural.utils.JwtUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationSender notificationSender;

    private final EmailNotificationRepository emailNotificationRepository;

    private final ContractNotificationRepository contractNotificationRepository;

    private final NotificationMapper notificationMapper;

    public void saveAndSendEmailNotification(EmailNotificationRequest emailNotificationRequest) {
        EmailNotification notification = notificationMapper.toEntity(emailNotificationRequest);
        EmailNotification savedNotification = emailNotificationRepository.save(notification);

        try {
            notificationSender.sendNotification(savedNotification);
            savedNotification.setIsSending(true);
        } catch (RuntimeException e) {
            log.error("Error while sending notification", e);
            savedNotification.setIsSending(false);
        } finally {
            emailNotificationRepository.save(savedNotification);
        }
    }

    public void createNotification(NotificationContractRequest request) {
        if (request.getUserUuids() == null || request.getUserUuids().isEmpty()) {
            return;
        }

        ContractNotification notification = notificationMapper.toEntity(request);
        contractNotificationRepository.save(notification);
    }

    public List<NotificationContractDto> getNotifications() {
        UserPrincipals principals = getUser();
        String uuid = Optional.ofNullable(principals)
                .map(UserPrincipals::getUuid)
                .orElse(null);

        if (uuid == null) {
            return List.of();
        }

        List<ContractNotification> notifications = contractNotificationRepository.findAllByUserUuidsContains(uuid);
        return notificationMapper.toDto(notifications);
    }

    @Transactional
    public void markAsRead(Long id) {
        ContractNotification notification = contractNotificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Уведомление с id: %d не найдено".formatted(id)));
        notification.setIsRead(true);
        contractNotificationRepository.save(notification);
    }

    private UserPrincipals getUser() {
        Authentication authentication = JwtUtils.getAuthentication();
        return JwtUtils.getUser(authentication);
    }

}
