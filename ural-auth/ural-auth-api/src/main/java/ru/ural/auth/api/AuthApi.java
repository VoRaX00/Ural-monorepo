package ru.ural.auth.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ural.auth.dto.LoginDto;
import ru.ural.auth.dto.UserDto;
import ru.ural.auth.dto.TokenRequest;
import ru.ural.auth.dto.AuthDto;

@RequestMapping("/api/auth")
@Tag(name = "Контроллер для Аутентификации")
public interface AuthApi {

    @Operation(summary = "Логин")
    @PostMapping("/login")
    ResponseEntity<AuthDto> login(@RequestBody LoginDto loginDto);

    @Operation(summary = "Регистрация")
    @PostMapping("/registration")
    ResponseEntity<AuthDto> registration(@RequestBody UserDto userDto);

    @Operation(summary = "Обновить токены")
    @PostMapping("/refresh-tokens")
    ResponseEntity<AuthDto> refreshTokens(@RequestBody TokenRequest tokenRequest);

    @Operation(summary = "Выход из текущей сессии")
    @PostMapping("/logout")
    ResponseEntity<Void> logout();

    @Operation(summary = "Выход из всех сессий")
    @PostMapping("/logout/all")
    ResponseEntity<Void> logoutAll();

}
