package ru.ural.contracts.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.ural.contracts.clients.dto.GraphHopperGeocodeResponse;
import ru.ural.contracts.clients.dto.GraphHopperRouteResponse;
import ru.ural.contracts.properties.GraphHopperProperties;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class GraphHopperClient {

    private final RestClient.Builder restClientBuilder;

    private final GraphHopperProperties properties;

    public GraphHopperGeocodeResponse geocode(String address) {
        return restClientBuilder
                .baseUrl(properties.getBaseUrl())
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/geocode")
                        .queryParam("q", address)
                        .queryParam("locale", properties.getLocale())
                        .queryParam("key", properties.getApiKey())
                        .build())
                .retrieve()
                .body(GraphHopperGeocodeResponse.class);
    }

    public GraphHopperRouteResponse buildRoute(
            BigDecimal fromLatitude,
            BigDecimal fromLongitude,
            BigDecimal toLatitude,
            BigDecimal toLongitude
    ) {
        return restClientBuilder
                .baseUrl(properties.getBaseUrl())
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/route")
                        .queryParam("point", toPoint(fromLatitude, fromLongitude), toPoint(toLatitude, toLongitude))
                        .queryParam("profile", properties.getProfile())
                        .queryParam("locale", properties.getLocale())
                        .queryParam("calc_points", true)
                        .queryParam("points_encoded", false)
                        .queryParam("key", properties.getApiKey())
                        .build())
                .retrieve()
                .body(GraphHopperRouteResponse.class);
    }

    private String toPoint(BigDecimal latitude, BigDecimal longitude) {
        return "%s,%s".formatted(latitude, longitude);
    }

}
