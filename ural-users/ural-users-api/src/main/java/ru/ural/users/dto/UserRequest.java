package ru.ural.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank
    @Schema(description = "Эл. почта")
    private String email;

    @NotBlank
    @Schema(description = "Имя")
    private String firstName;

    @NotBlank
    @Schema(description = "Фамилия")
    private String lastName;

    @Schema(description = "Отчество")
    private String patronymic;

    @Schema(description = "Номер телефона")
    private String phoneNumber;

    @Schema(description = "Аватар")
    private AvatarRequest avatar;

}
