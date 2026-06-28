package ru.ural.users.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.ural.users.dto.UserDto;
import ru.ural.users.dto.UserRegistration;
import ru.ural.users.dto.UserRequest;
import ru.ural.users.entities.User;
import ru.ural.users.models.RegistrationModel;
import ru.ural.users.models.UserModel;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = AvatarMapper.class
)
public interface UserMapper {

    User toEntity(UserModel userModel);

    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "ratingsCount", ignore = true)
    UserModel toModel(User user);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "ratingsCount", ignore = true)
    RegistrationModel toModel(UserRegistration userRegistration);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "ratingsCount", ignore = true)
    UserModel toModel(UserRequest userRequest);

    UserDto toDto(UserModel userModel);

    ru.ural.auth.dto.UserDto toAuthUserDto(RegistrationModel user);

    @Mapping(target = "uuid", ignore = true)
    void mapModelToEntity(@MappingTarget User user, UserModel userModel);

}
