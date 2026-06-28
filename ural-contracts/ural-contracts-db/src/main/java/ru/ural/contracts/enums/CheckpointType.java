package ru.ural.contracts.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CheckpointType {

    LOADING("Погрузка"),
    INTERMEDIATE("Промежуточная точка"),
    UNLOADING("Разгрузка");

    private final String value;

}
