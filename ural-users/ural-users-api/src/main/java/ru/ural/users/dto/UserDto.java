package ru.ural.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Schema(description = "UUID пользователя")
    private UUID uuid;

    @Schema(description = "Эл. почта")
    private String email;

    @Schema(description = "Имя")
    private String firstName;

    @Schema(description = "Фамилия")
    private String lastName;

    @Schema(description = "Отчество")
    private String patronymic;

    @Schema(description = "Номер телефона")
    private String phoneNumber;

    @Schema(description = "Аватар пользователя")
    private AvatarDto avatar;

    @Schema(description = "Средний рейтинг пользователя")
    private BigDecimal averageRating;

    @Schema(description = "Количество оценок пользователя")
    private Long ratingsCount;
}
