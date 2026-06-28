package ru.ural.ai.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;
import ru.ural.cargo.dto.CargoDto;
import ru.ural.dto.PageDto;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class CargoServiceClient {

    private static final int RECOMMENDATION_PAGE_SIZE = 500;

    private static final ParameterizedTypeReference<PageDto<CargoDto>> CARGO_PAGE_TYPE =
            new ParameterizedTypeReference<>() {
            };

    @Qualifier("cargoServiceRestClient")
    private final RestClient cargoServiceRestClient;

    public CargoDto getCargoById(Long id, String authorization) {
        return cargoServiceRestClient.get()
                .uri("/api/cargo/{id}", id)
                .headers(headers -> addAuthorizationHeader(headers, authorization))
                .retrieve()
                .body(CargoDto.class);
    }

    public PageDto<CargoDto> getCargoPageByUserUuid(String userUuid, String authorization) {
        return cargoServiceRestClient.get()
                .uri(uriBuilder -> buildPageUri(uriBuilder, userUuid))
                .headers(headers -> addAuthorizationHeader(headers, authorization))
                .retrieve()
                .body(CARGO_PAGE_TYPE);
    }

    private URI buildPageUri(UriBuilder uriBuilder, String userUuid) {
        return uriBuilder.path("/api/cargo")
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
