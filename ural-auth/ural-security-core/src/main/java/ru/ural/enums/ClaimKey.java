package ru.ural.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClaimKey {

    USER_UUID_KEY("user_uuid"),
    ROLES_KEY("roles");

    private final String key;

}
