package ru.ural.contracts.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.ural.dto.PaginatedParamsDto;
import ru.ural.models.PaginatedParamsModel;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaginatedMapper {

    PaginatedParamsModel toModel(PaginatedParamsDto paramsDto);

}
