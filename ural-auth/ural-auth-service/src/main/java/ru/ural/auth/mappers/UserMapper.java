package ru.ural.auth.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.ural.auth.entities.User;
import ru.ural.auth.models.UserModel;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    User toEntity(UserModel userModel);

}
