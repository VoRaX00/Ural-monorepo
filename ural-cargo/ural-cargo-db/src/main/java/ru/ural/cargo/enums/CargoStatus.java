package ru.ural.cargo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.ural.entities.ValuableEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Getter
@AllArgsConstructor
public enum CargoStatus implements ValuableEnum<CargoStatus> {

    SEARCH("В поиске"),
    AWAITING("Ожидает перевозки"),
    PROCESS("В процессе перевозки"),
    DELIVERED("Доставлен");

    private final String value;

    public static CargoStatus parse(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Cargo status is empty");
        }

        String normalizedValue = value.trim();
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(normalizedValue)
                        || status.getValue().toLowerCase(Locale.ROOT)
                        .equals(normalizedValue.toLowerCase(Locale.ROOT)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown cargo status: " + value));
    }

    @Override
    public List<CargoStatus> getValues() {
        return Arrays.stream(CargoStatus.values()).toList();
    }
}
