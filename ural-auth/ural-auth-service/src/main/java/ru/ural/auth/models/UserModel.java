package ru.ural.auth.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

    private UUID uuid;

    private String login;

    private String lastName;

    private String firstName;

    private String patronymic;

    private String password;

}
