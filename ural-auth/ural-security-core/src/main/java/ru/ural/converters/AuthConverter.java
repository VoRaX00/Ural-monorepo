package ru.ural.converters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import ru.ural.services.RoleSecurityService;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class AuthConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String DEFAULT_ROLE = "URAL_ANY";

    private final RoleSecurityService roleSecurityService;

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Set<GrantedAuthority> tokenRoles = roleSecurityService.getRoles(jwt.getClaims()).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        tokenRoles.add(new SimpleGrantedAuthority(DEFAULT_ROLE));
        return tokenRoles;
    }

}
