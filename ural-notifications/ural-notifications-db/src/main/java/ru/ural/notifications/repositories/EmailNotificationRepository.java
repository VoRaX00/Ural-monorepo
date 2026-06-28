package ru.ural.notifications.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ural.notifications.entities.EmailNotification;

public interface EmailNotificationRepository extends JpaRepository<EmailNotification, Long> {
}
