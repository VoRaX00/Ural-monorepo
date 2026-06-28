package ru.ural.users.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.ural.users.api.UsersApi;
import ru.ural.auth.dto.AuthDto;
import ru.ural.users.dto.UserDto;
import ru.ural.users.dto.UserRegistration;
import ru.ural.users.dto.UserRequest;
import ru.ural.users.mappers.UserMapper;
import ru.ural.users.models.RegistrationModel;
import ru.ural.users.models.UserModel;
import ru.ural.users.services.UserService;

@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

    private final UserService userService;

    private final UserMapper userMapper;

    @Override
    public ResponseEntity<AuthDto> registration(UserRegistration userRegistration) {
        RegistrationModel requestModel = userMapper.toModel(userRegistration);
        return ResponseEntity.ok(userService.create(requestModel));
    }

    @Override
    public ResponseEntity<UserDto> getByUuid(String uuid) {
        UserModel model = userService.getByUuid(uuid);
        return ResponseEntity.ok(userMapper.toDto(model));
    }

    @Override
    public ResponseEntity<UserDto> update(String uuid, UserRequest userRequest) {
        UserModel requestModel = userMapper.toModel(userRequest);
        UserModel updatedUser = userService.updateUser(uuid, requestModel);
        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }

}
