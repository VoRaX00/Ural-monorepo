package ru.ural.cargo.mappers;

import org.mapstruct.*;
import ru.ural.cargo.dto.CargoDto;
import ru.ural.cargo.dto.CargoRequest;
import ru.ural.cargo.entities.Cargo;
import ru.ural.cargo.enums.BodyType;
import ru.ural.cargo.enums.CargoStatus;
import ru.ural.cargo.enums.LoadingType;

import java.util.List;
import java.util.Objects;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = AddressMapper.class
)
public interface CargoMapper {

    @Mapping(target = "status", source = "status", qualifiedByName = "mapCargoStatusToValue")
    @Mapping(target = "bodyTypes", source = "bodyTypes", qualifiedByName = "mapBodyTypesToValues")
    @Mapping(target = "loadingTypes", source = "loadingTypes", qualifiedByName = "mapLoadingTypesToValues")
    @Mapping(target = "unloadingTypes", source = "unloadingTypes", qualifiedByName = "mapLoadingTypesToValues")
    CargoDto toDto(Cargo cargo);

    List<CargoDto> toDto(List<Cargo> cargos);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "userUuid", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "bodyTypes", source = "bodyTypes", qualifiedByName = "mapBodyTypeValues")
    @Mapping(target = "loadingTypes", source = "loadingTypes", qualifiedByName = "mapLoadingTypeValues")
    @Mapping(target = "unloadingTypes", source = "unloadingTypes", qualifiedByName = "mapLoadingTypeValues")
    Cargo toEntity(CargoRequest request);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "userUuid", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "bodyTypes", source = "bodyTypes", qualifiedByName = "mapBodyTypeValues")
    @Mapping(target = "loadingTypes", source = "loadingTypes", qualifiedByName = "mapLoadingTypeValues")
    @Mapping(target = "unloadingTypes", source = "unloadingTypes", qualifiedByName = "mapLoadingTypeValues")
    void mapCargoDtoToEntity(@MappingTarget Cargo cargo, CargoRequest cargoRequest);

    @Named("mapCargoStatus")
    default CargoStatus mapCargoStatus(String value) {
        return CargoStatus.parse(value);
    }

    @Named("mapCargoStatusToValue")
    default String mapCargoStatusToValue(CargoStatus status) {
        return status == null ? CargoStatus.SEARCH.getValue() : status.getValue();
    }

    @Named("mapBodyTypeValues")
    default List<BodyType> mapBodyTypeValues(List<String> values) {
        if (values == null) {
            return List.of();
        }

        return values.stream()
                .map(BodyType::parse)
                .distinct()
                .toList();
    }

    @Named("mapLoadingTypeValues")
    default List<LoadingType> mapLoadingTypeValues(List<String> values) {
        if (values == null) {
            return List.of();
        }

        return values.stream()
                .map(LoadingType::parse)
                .distinct()
                .toList();
    }

    @Named("mapBodyTypesToValues")
    default List<String> mapBodyTypesToValues(List<BodyType> values) {
        if (values == null) {
            return List.of();
        }

        return values.stream()
                .filter(Objects::nonNull)
                .map(BodyType::getValue)
                .toList();
    }

    @Named("mapLoadingTypesToValues")
    default List<String> mapLoadingTypesToValues(List<LoadingType> values) {
        if (values == null) {
            return List.of();
        }

        return values.stream()
                .filter(Objects::nonNull)
                .map(LoadingType::getValue)
                .toList();
    }

}
