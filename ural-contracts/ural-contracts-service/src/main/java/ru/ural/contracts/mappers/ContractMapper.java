package ru.ural.contracts.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import ru.ural.cargo.dto.CargoDto;
import ru.ural.cars.dto.CarDto;
import ru.ural.contracts.dto.ContractDto;
import ru.ural.contracts.dto.ContractRequest;
import ru.ural.contracts.dto.RouteDto;
import ru.ural.contracts.entities.Contract;
import ru.ural.contracts.entities.Route;
import ru.ural.contracts.models.ContractModel;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContractMapper {

    ContractDto toDto(ContractModel contract);

    @Mapping(target = "cargo", source = "cargoId", qualifiedByName = "mapCargo")
    @Mapping(target = "car", source = "carId", qualifiedByName = "mapCar")
    @Mapping(target = "status", expression = "java(contract.getStatus().getValue())")
    ContractDto toDto(Contract contract);

    @Mapping(target = "contractId", source = "contract.id")
    RouteDto toDto(Route route);

    List<ContractDto> toDto(List<Contract> contract);

    @Mapping(target = "ownerUuid", ignore = true)
    @Mapping(target = "relatedUserUuid", ignore = true)
    @Mapping(target = "priceRequestedByUuid", ignore = true)
    @Mapping(target = "priceUpdatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "route", ignore = true)
    Contract toEntity(ContractRequest contractRequest);

    @Mapping(target = "car", ignore = true)
    @Mapping(target = "cargo", ignore = true)
    @Mapping(target = "status", expression = "java(contract.getStatus().getValue())")
    ContractModel toModel(Contract contract);

    @Named("mapCargo")
    default CargoDto mapCargo(Long cargoId) {
        return CargoDto.builder()
                .id(cargoId)
                .build();
    }

    @Named("mapCar")
    default CarDto mapCar(Long carId) {
        return CarDto.builder()
                .id(carId)
                .build();
    }

}
