package ru.ural.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto для логина пользователя")
public class LoginDto {

    @Schema(description = "Логин")
    private String login;

    @Schema(description = "Пароль")
    private String password;

}
