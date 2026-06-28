package ru.ural.utils;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import ru.ural.enums.ClaimKey;
import ru.ural.enums.UserRole;
import ru.ural.models.UserPrincipals;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtUtils {

    private JwtUtils(){
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext()
                .getAuthentication();
    }

    @NonNull
    public static UserPrincipals getUser(@Nullable Authentication authentication) {
        Jwt jwt = getToken(authentication);
        return UserPrincipals.builder()
                .uuid(getClaim(jwt, ClaimKey.USER_UUID_KEY.getKey(), String.class))
                .roles(getRoles(authentication))
                .build();
    }

    @NonNull
    public static Jwt getToken(@Nullable Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken) {
            return ((JwtAuthenticationToken) authentication).getToken();
        }
        throw new IllegalArgumentException("Authentification not cast to JwtAuthenticationToken");
    }

    @NonNull
    public static Map<String, Object> getClaims(@Nullable Authentication authentication) {
        Jwt jwt = getToken(authentication);
        return jwt.getClaims();
    }

    @NonNull
    private static Set<UserRole> getRoles(@Nullable Authentication authentication) {
        if (authentication == null) {
            return Set.of();
        }

        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(UserRole::valueOf)
                .collect(Collectors.toSet());
    }

    private static <T> T getClaim(Jwt jwt, String key, Class<T> clazz) {
        Object object = jwt.getClaims().getOrDefault(key, null);
        return Optional.ofNullable(object)
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .orElse(null);
    }

}
