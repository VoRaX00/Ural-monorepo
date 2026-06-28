package ru.ural.contracts.senders;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.ural.cars.dto.CarDto;
import ru.ural.models.HttpResponse;
import ru.ural.contracts.properties.ContractIntegrationProperty;
import ru.ural.services.RestSender;
import ru.ural.utils.JwtUtils;

import java.net.URI;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CarsSender {

    private static final String PATTERN_GET_CAR_BY_ID = "%s/%s";

    private static final String PATTERN_UPDATE_CAR_BOOKING = "%s/%s/booking";

    private final RestSender restSender;

    private final ContractIntegrationProperty contractIntegrationProperty;

    public CarDto getCarById(Long id) {
        String baseUrl = String.format(
                PATTERN_GET_CAR_BY_ID,
                contractIntegrationProperty.getCarsUrl(),
                id
        );

        URI uri = RestSender.encodeUrl(baseUrl, null);
        HttpResponse<CarDto> response = restSender.sendRequest(
                uri,
                HttpMethod.GET,
                new ParameterizedTypeReference<>() { },
                JwtUtils.getToken(JwtUtils.getAuthentication()).getTokenValue()
        );

        return response.body();
    }

    public CarDto updateCarBooking(Long id, Boolean booked) {
        String baseUrl = String.format(
                PATTERN_UPDATE_CAR_BOOKING,
                contractIntegrationProperty.getCarsUrl(),
                id
        );

        URI uri = RestSender.encodeUrl(baseUrl, Map.of("booked", booked));
        HttpResponse<CarDto> response = restSender.sendRequest(
                uri,
                HttpMethod.PUT,
                new ParameterizedTypeReference<>() { },
                JwtUtils.getToken(JwtUtils.getAuthentication()).getTokenValue()
        );

        return response.body();
    }

}
