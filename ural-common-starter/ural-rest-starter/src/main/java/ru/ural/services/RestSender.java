package ru.ural.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.ural.exceptions.InternalServerException;
import ru.ural.mappers.HttpExceptionMapper;
import ru.ural.models.HttpResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestSender {

    private static final String BEARER_TOKEN_PREFIX = "Bearer %s";

    private final RestTemplate restTemplate;

    private final HttpExceptionMapper httpExceptionMapper;

    public <T> HttpResponse<T> sendRequest(
            @NonNull URI url,
            @NonNull HttpMethod method,
            @NonNull ParameterizedTypeReference<T> typeReference,
            @Nullable HttpEntity<?> entity
    ) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(
                    url,
                    method,
                    entity,
                    typeReference
            );

            return new HttpResponse<>(response.getBody(), response.getStatusCode(), null);
        } catch (HttpStatusCodeException e) {
            log.error("Error send request on url: {} statusCode: {}", url, e.getStatusCode().value(), e);
            throw httpExceptionMapper.map(e);
        } catch (RestClientException e) {
            log.error("Error send request on url: {}", url, e);
            throw new InternalServerException(e.getMessage(), e);
        }
    }

    public <T> HttpResponse<T> sendRequest(
            @NonNull URI url,
            @NonNull HttpMethod method,
            @NonNull ParameterizedTypeReference<T> typeReference,
            @NonNull String token
    ) {
        try {
            String bearerToken = BEARER_TOKEN_PREFIX.formatted(token);
            HttpHeaders headers = new HttpHeaders(MultiValueMap.fromSingleValue(
                    Map.of(HttpHeaders.AUTHORIZATION, bearerToken)
            ));

            ResponseEntity<T> response = restTemplate.exchange(
                    url,
                    method,
                    new HttpEntity<>(headers),
                    typeReference
            );

            return new HttpResponse<>(response.getBody(), response.getStatusCode(), null);
        } catch (HttpStatusCodeException e) {
            log.error("Error send request on url: {} statusCode: {}", url, e.getStatusCode().value(), e);
            throw httpExceptionMapper.map(e);
        } catch (RestClientException e) {
            log.error("Error send request on url: {}", url, e);
            throw new InternalServerException(e.getMessage(), e);
        }
    }

    public static URI encodeUrl(@NonNull String url, @Nullable Map<String, Object> pathParams) {
        try {
            URI uri = new URI(url);
            UriComponentsBuilder builder = UriComponentsBuilder.fromUri(uri);

            if (pathParams != null) {
                pathParams.forEach((param, value) -> builder.queryParam(param, String.valueOf(value)));
            }

            return builder.encode()
                    .build()
                    .toUri();
        } catch (URISyntaxException e) {
            log.error("Error with generate url with params, url: {}", url);
            throw new InternalServerException(e.getMessage(), e);
        }
    }

}
