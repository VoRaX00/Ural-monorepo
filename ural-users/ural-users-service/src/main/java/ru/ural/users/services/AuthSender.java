package ru.ural.users.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.ural.auth.dto.AuthDto;
import ru.ural.auth.dto.UserDto;
import ru.ural.models.HttpResponse;
import ru.ural.services.RestSender;
import ru.ural.users.properties.AuthProperty;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthSender {

    private final RestSender restSender;

    private final AuthProperty authProperty;

    @NonNull
    public AuthDto registration(@NonNull UserDto userDto) {
        HttpEntity<UserDto> httpEntity = new HttpEntity<>(userDto);

        URI uri = RestSender.encodeUrl(authProperty.getPathRegistration(), null);
        HttpResponse<AuthDto> authDto = restSender.sendRequest(
                uri,
                HttpMethod.POST,
                new ParameterizedTypeReference<>() { },
                httpEntity
        );

        return authDto.body();
    }

}
