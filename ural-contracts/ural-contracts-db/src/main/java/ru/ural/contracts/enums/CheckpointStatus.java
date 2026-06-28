package ru.ural.contracts.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CheckpointStatus {

    PENDING("Ожидает прибытия"),
    ARRIVED("Ожидает подтверждения"),
    CONFIRMED("Подтверждена"),
    REJECTED("Отклонена");

    private final String value;

}
