package ru.ural.users.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

    private UUID uuid;

    private String email;

    private String firstName;

    private String lastName;

    private String patronymic;

    private String phoneNumber;

    private AvatarModel avatar;

    private BigDecimal averageRating;

    private Long ratingsCount;

}
