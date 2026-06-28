package ru.ural.notifications.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.ural.notifications.dto.contract.NotificationContractDto;
import ru.ural.notifications.dto.email.EmailNotificationDto;
import ru.ural.notifications.dto.email.EmailNotificationRequest;
import ru.ural.notifications.dto.contract.NotificationContractRequest;
import ru.ural.notifications.entities.ContractNotification;
import ru.ural.notifications.entities.EmailNotification;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationMapper {

    @Mapping(target = "isSending", ignore = true)
    EmailNotification toEntity(EmailNotificationRequest emailNotificationRequest);

    EmailNotificationDto toDto(EmailNotification emailNotification);

    @Mapping(target = "isRead", ignore = true)
    ContractNotification toEntity(NotificationContractRequest request);

    NotificationContractDto toDto(ContractNotification notification);

    List<NotificationContractDto> toDto(List<ContractNotification> notifications);

}
