package ru.ural.cars.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.util.CollectionUtils;
import ru.ural.cars.dto.CarDto;
import ru.ural.cars.dto.CarRequest;
import ru.ural.cars.entities.Car;
import ru.ural.cars.enums.BodyType;
import ru.ural.cars.enums.CarConditionStatus;
import ru.ural.cars.enums.CarType;
import ru.ural.cars.enums.LoadingType;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CarMapper {

    @Mapping(target = "carType", expression = "java(car.getCarType().getValue())")
    @Mapping(target = "photoAnalysisStatus", source = "photoAnalysisStatus", qualifiedByName = "mapStatusToDto")
    @Mapping(target = "bodyType", source = "bodyType", qualifiedByName = "mapBodyTypeToDto")
    @Mapping(target = "loadingType", source = "loadingType", qualifiedByName = "mapLoadingTypeDto")
    CarDto toDto(Car car);

    List<CarDto> toDto(List<Car> cars);

    @Mapping(target = "userUuid", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "booked", ignore = true)
    @Mapping(target = "carType", source = "carType", qualifiedByName = "mapCarType")
    @Mapping(target = "bodyType", source = "bodyType", qualifiedByName = "mapBodyType")
    @Mapping(target = "loadingType", source = "loadingType", qualifiedByName = "mapLoadingType")
    Car toEntity(CarRequest car);

    List<Car> toEntity(List<Car> cars);

    @Mapping(target = "carType", source = "carType", qualifiedByName = "mapCarType")
    @Mapping(target = "booked", ignore = true)
    @Mapping(target = "bodyType", source = "bodyType", qualifiedByName = "mapBodyType")
    @Mapping(target = "loadingType", source = "loadingType", qualifiedByName = "mapLoadingType")
    void mapCarDtoToCar(CarRequest carDto, @MappingTarget Car car);

    @Named("mapCarType")
    default CarType mapCarType(String value) {
        return CarType.parse(value);
    }

    @Named("mapStatusToDto")
    default String mapStatusToDto(CarConditionStatus status) {
        return status == null ? null : status.name();
    }

    @Named("mapBodyType")
    default List<BodyType> mapBodyType(List<String> value) {
        return value.stream()
                .map(BodyType::parse)
                .toList();
    }

    @Named("mapLoadingType")
    default List<LoadingType> mapLoadingType(List<String> value) {
        return value.stream()
                .map(LoadingType::parse)
                .toList();
    }

    @Named("mapBodyTypeToDto")
    default List<String> mapBodyTypeToDto(List<BodyType> value) {
        if (CollectionUtils.isEmpty(value)) {
            return List.of();
        }

        return value.stream()
                .map(BodyType::getValue)
                .toList();
    }

    @Named("mapLoadingTypeDto")
    default List<String> mapLoadingTypeToDto(List<LoadingType> value) {
        if (CollectionUtils.isEmpty(value)) {
            return List.of();
        }

        return value.stream()
                .map(LoadingType::getValue)
                .toList();
    }

}
