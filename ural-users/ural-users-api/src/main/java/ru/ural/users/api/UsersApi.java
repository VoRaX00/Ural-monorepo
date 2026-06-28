package ru.ural.users.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ural.auth.dto.AuthDto;
import ru.ural.users.dto.UserDto;
import ru.ural.users.dto.UserRegistration;
import ru.ural.users.dto.UserRequest;

@RequestMapping("/api/users")
@Tag(name = "UsersController", description = "Контроллер для работы с пользователями")
public interface UsersApi {

    @Operation(summary = "Регистрация")
    @PostMapping("/registration")
    ResponseEntity<AuthDto> registration(@RequestBody UserRegistration userRegistration);

    @Operation(summary = "Получить пользователя по uuid")
    @GetMapping("/{uuid}")
    ResponseEntity<UserDto> getByUuid(@PathVariable String uuid);

    @Operation(summary = "Обновить информацию о пользователе")
    @PutMapping("/{uuid}")
    ResponseEntity<UserDto> update(@PathVariable String uuid, @RequestBody UserRequest userRequest);

}
