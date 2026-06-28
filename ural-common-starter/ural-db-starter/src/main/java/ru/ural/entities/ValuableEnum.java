package ru.ural.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface ValuableEnum<T extends ValuableEnum<T>> {

    Logger LOG = LoggerFactory.getLogger(ValuableEnum.class);

    default T parseValue(String value) {
        if (value == null) {
            return null;
        }

        List<T> foundValues = getValues().stream()
                .filter(v -> value.equals(v.getValue()))
                .toList();

        if (foundValues.size() > 1) {
            LOG.warn("Several founded values for enum value: {}", value);
        }

        return foundValues.isEmpty()
                ? null
                : foundValues.getFirst();
    }

    String getValue();

    List<T> getValues();

}
