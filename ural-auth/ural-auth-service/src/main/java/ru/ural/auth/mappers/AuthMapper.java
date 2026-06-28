package ru.ural.auth.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.ural.auth.dto.AuthDto;
import ru.ural.auth.dto.LoginDto;
import ru.ural.auth.dto.UserDto;
import ru.ural.auth.models.AuthModel;
import ru.ural.auth.models.LoginModel;
import ru.ural.auth.models.UserModel;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthMapper {

    LoginModel toModel(LoginDto loginDto);

    AuthDto toDto(AuthModel authModel);

    UserModel toModel(UserDto userDto);

}
