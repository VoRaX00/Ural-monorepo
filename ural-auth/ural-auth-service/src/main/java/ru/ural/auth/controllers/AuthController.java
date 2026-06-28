package ru.ural.auth.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.ural.auth.api.AuthApi;
import ru.ural.auth.dto.LoginDto;
import ru.ural.auth.dto.UserDto;
import ru.ural.auth.dto.TokenRequest;
import ru.ural.auth.dto.AuthDto;
import ru.ural.auth.mappers.AuthMapper;
import ru.ural.auth.services.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    private final AuthMapper authMapper;

    @Override
    public ResponseEntity<AuthDto> login(LoginDto loginDto) {
        var loginModel = authMapper.toModel(loginDto);
        var authModel = authService.login(loginModel);
        return ResponseEntity.ok(authMapper.toDto(authModel));
    }

    @Override
    public ResponseEntity<AuthDto> registration(UserDto userDto) {
        var user = authMapper.toModel(userDto);
        var authModel = authService.registration(user);
        return ResponseEntity.ok(authMapper.toDto(authModel));
    }

    @Override
    public ResponseEntity<AuthDto> refreshTokens(TokenRequest tokenRequest) {
        var authModel = authService.refreshTokens(tokenRequest.getRefreshToken());
        return ResponseEntity.ok(authMapper.toDto(authModel));
    }

    @Override
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> logoutAll() {
        authService.logoutAll();
        return ResponseEntity.ok().build();
    }
}
