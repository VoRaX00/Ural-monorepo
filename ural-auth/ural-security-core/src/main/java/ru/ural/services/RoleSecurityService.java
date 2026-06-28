package ru.ural.services;

import lombok.RequiredArgsConstructor;
import ru.ural.enums.ClaimKey;

import java.util.*;

@RequiredArgsConstructor
public class RoleSecurityService {

    public Set<String> getRoles(Map<String, Object> claims) {
        return new HashSet<>(Optional.ofNullable(claims)
                .map(cl -> cl.get(ClaimKey.ROLES_KEY.getKey()))
                .map(ra -> (List<String>) ra)
                .orElse(Collections.emptyList())
        );
    }

}
