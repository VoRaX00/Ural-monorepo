package ru.ural.cars.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.ural.entities.ValuableEnum;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum CarType implements ValuableEnum<CarType> {

    PASSENGER("Легковой автомобиль"),
    CARGO_TRUCK("Грузовой автомобиль");

    private final String value;

    public static CarType parse(String value) {
        if (value == null) {
            return null;
        }

        String normalizedValue = value.trim();
        return Arrays.stream(values())
                .filter(carType -> carType.name().equalsIgnoreCase(normalizedValue)
                        || carType.getValue().equalsIgnoreCase(normalizedValue))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<CarType> getValues() {
        return Arrays.stream(CarType.values()).toList();
    }

}
