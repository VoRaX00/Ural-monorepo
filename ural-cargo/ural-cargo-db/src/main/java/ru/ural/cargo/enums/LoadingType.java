package ru.ural.cargo.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.ural.entities.ValuableEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Getter
@RequiredArgsConstructor
public enum LoadingType implements ValuableEnum<LoadingType> {

    UPPER("верхняя"),
    LATERAL("боковая"),
    BACK("задняя"),
    FULL_RASTERIZATION("с полной растентовкой"),
    REMOVAL_CROSSBAR("со снятием поперечных перекладин"),
    REMOVAL_RACKS("со снятием стоек"),
    WITHOUT_GATE("без ворот"),
    HYDROBOARD("гидроборт"),
    RAMPS("аппарели"),
    WITH_CRATE("с обрешеткой"),
    WITH_SIDES("с бортами"),
    SIDE_FROM_2_SIDES("боковая с 2-х сторон"),
    FILLING("налив"),
    ELECTRIC("электрический"),
    HYDRAULIC("гидравлический"),
    UNDEFINED("не указан"),
    PNEUMATIC("пневматический"),
    DIESEL_COMPRESSOR("дизельный компрессор");

    private final String value;

    public static LoadingType parse(String value) {
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

    @Override
    public List<LoadingType> getValues() {
        return Arrays.asList(values());
    }

}
