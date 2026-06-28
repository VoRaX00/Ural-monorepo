package ru.ural.auth.utils;

import org.jspecify.annotations.NonNull;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class AuthUtils {

    @NonNull
    public static String hashPassword(@NonNull String password, @NonNull String salt) {
        return BCrypt.hashpw(password, salt);
    }

    @NonNull
    public static String generateSalt() {
        return BCrypt.gensalt();
    }

}
