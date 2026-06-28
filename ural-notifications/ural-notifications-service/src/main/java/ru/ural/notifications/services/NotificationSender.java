package ru.ural.notifications.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.ural.notifications.entities.EmailNotification;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationSender {

    private final JavaMailSender mailSender;

    private final MailProperties mailProperties;

    public void sendNotification(EmailNotification notification) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(notification.getEmail());
        message.setSubject(notification.getTitle());
        message.setText(notification.getBody());

        if (StringUtils.hasText(mailProperties.getUsername())) {
            message.setFrom(mailProperties.getUsername());
        }

        mailSender.send(message);
        log.info("Notification {} sent to {}", notification.getId(), notification.getEmail());
    }

}
