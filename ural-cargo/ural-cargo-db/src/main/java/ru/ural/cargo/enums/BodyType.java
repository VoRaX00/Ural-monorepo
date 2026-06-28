package ru.ural.cargo.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.ural.entities.ValuableEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Getter
@RequiredArgsConstructor
public enum BodyType implements ValuableEnum<BodyType> {

    AWNING("тентованный"),
    CONTAINER("контейнер"),
    VAN("фургон"),
    ALL_METAL("цельнометал."),
    ISOTHERMAL("изотермический"),
    REFRIGERATOR_MULTI_TEMP("реф. мультирежимный"),
    REFRIGERATOR_WITH_PARTITION("реф. с перегородкой"),
    REFRIGERATED_TRUCK("реф.-тушевоз"),
    ON_BOARD("бортовой"),
    OPEN_CONTAINER("открытый контейнер"),
    PLATFORM_WITHOUT_SIDES("площадка без бортов"),
    DUMP_TRUCK("самосвал"),
    SCOW("шаланда"),
    LOW_FRAME("низкорамный"),
    LOW_PLATFORM("низкорам.платф."),
    TELESCOPIC("телескопический"),
    TRAWL("трал"),
    BULK("балковоз(негабарит)"),
    BUS("автобус"),
    AUTOTOWER("автовышка"),
    AUTO_TRANSPORTER("автотранспортер"),
    CONCRETE_TRUCK("бетоновоз"),
    BITUMEN_TRUCK("битумовоз"),
    FUEL_TRUCKER("бензовоз"),
    ALL_TERRAIN("вездеход"),
    GAS_CARRIER("газовоз"),
    GRAIN_CARRIER("зерновоз"),
    HORSE_CARRIER("коневоз"),
    CONTAINER_CARRIER("контейнеровоз"),
    FEED_TRUCK("кормовоз"),
    CRANE("кран"),
    TIMBER_CARRIER("лесовоз"),
    SCARP_TRUCK("ломовоз"),
    MANIPULATOR("манипулятор"),
    MINIBUS("микроавтобус"),
    FLOUR_TRUCK("муковоз"),
    PANEL_TRUCK("панелевоз"),
    PICKUP_TRUCK("пикап"),
    DOWN_TRUCK("пухтовоз"),
    PYRAMID("пирамида"),
    ROLL_TRUCK("рулоновоз"),
    TRACTOR_TRUCK("седельный тягач"),
    CATTLE_TRUCK("скотовоз"),
    GLASS_CARRIER("стекловоз"),
    PIPE_CARRIER("трубовоз"),
    CEMENT_TRUCK("цементовоз"),
    TANKER_TRUCK("автоцистерна"),
    WOODCHIPPER_TRUCK("щеповоз"),
    TOW_TRUCK("эвакуатор"),
    CARGO_PASSENGER("грузопассажирский"),
    STICK_CARRIER("клюшковоз"),
    GARBAGE_TRUCK("мусоровоз"),
    JUMBO("jumbo"),
    TANK_CONTAINER_20("20' танк-контейнер"),
    TANK_CONTAINER_40("40' танк-контейнер"),
    MEGA_TRUCK("мега фура"),
    DOPPELGANGER("допельшток"),
    SLIDING_SEMI_TRAILER("Раздвижной полуприцеп 20'/40'");

    private final String value;

    @Override
    public List<BodyType> getValues() {
        return Arrays.asList(values());
    }

    public static BodyType parse(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Body type is empty");
        }

        String normalizedValue = value.trim();
        return Arrays.stream(values())
                .filter(bodyType -> bodyType.name().equalsIgnoreCase(normalizedValue)
                        || bodyType.getValue().toLowerCase(Locale.ROOT)
                                .equals(normalizedValue.toLowerCase(Locale.ROOT)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown body type: " + value));
    }

}
