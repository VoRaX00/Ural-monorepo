package ru.ural.contracts.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.ural.entities.ValuableEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Getter
@RequiredArgsConstructor
public enum ContractStatus implements ValuableEnum<ContractStatus> {

    AGREEMENT("На согласовании"),
    READY_EXECUTION("Готов к исполнению"),
    PROCESS("В процессе"),
    FINISHED("Завершен"),
    CLOSED("Отклонен");

    private final String value;

    @Override
    public List<ContractStatus> getValues() {
        return Arrays.asList(values());
    }

    public static ContractStatus parse(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Loading type is empty");
        }

        String normalizedValue = value.trim();
        return Arrays.stream(values())
                .filter(loadingType -> loadingType.name().equalsIgnoreCase(normalizedValue)
                        || loadingType.getValue().toLowerCase(Locale.ROOT)
                        .equals(normalizedValue.toLowerCase(Locale.ROOT)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown loading type: " + value));
    }

}
