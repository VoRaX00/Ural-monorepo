package ru.ural.ai.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;
import ru.ural.cars.dto.CarDto;
import ru.ural.dto.PageDto;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class CarsServiceClient {

    private static final int RECOMMENDATION_PAGE_SIZE = 500;

    private static final ParameterizedTypeReference<PageDto<CarDto>> CAR_PAGE_TYPE =
            new ParameterizedTypeReference<>() {
            };

    @Qualifier("carsServiceRestClient")
    private final RestClient carsServiceRestClient;

    public CarDto getCarById(Long id, String authorization) {
        return carsServiceRestClient.get()
                .uri("/api/cars/{id}", id)
                .headers(headers -> addAuthorizationHeader(headers, authorization))
                .retrieve()
                .body(CarDto.class);
    }

    public PageDto<CarDto> getCarPageByUserUuid(String userUuid, String authorization) {
        return carsServiceRestClient.get()
                .uri(uriBuilder -> buildPageUri(uriBuilder, userUuid))
                .headers(headers -> addAuthorizationHeader(headers, authorization))
                .retrieve()
                .body(CAR_PAGE_TYPE);
    }

    private URI buildPageUri(UriBuilder uriBuilder, String userUuid) {
        return uriBuilder.path("/api/cars")
                .queryParam("currentPageNumber", 1)
                .queryParam("itemsOnPage", RECOMMENDATION_PAGE_SIZE)
                .queryParam("sorting", "id")
                .queryParam("sortingValue", "desc")
                .queryParam("filters[userUuid]", userUuid)
                .build();
    }

    private void addAuthorizationHeader(HttpHeaders headers, String authorization) {
        if (authorization != null && !authorization.isBlank()) {
            headers.set(HttpHeaders.AUTHORIZATION, authorization);
        }
    }

}
