package ru.ural.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto для регистрации")
public class UserDto {

    @Schema(description = "UUID пользователя")
    private UUID uuid;

    @Schema(description = "Логин")
    private String login;

    @Schema(description = "Пароль")
    private String password;

}
