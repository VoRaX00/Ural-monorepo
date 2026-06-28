package ru.ural.cars.clients;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.ural.cars.clients.dto.AutocodeInfoResponse;
import ru.ural.dto.InspectionDto;
import ru.ural.dto.accidents.AccidentInfoDto;
import ru.ural.dto.wanted.WantedInfoDto;

@Component
@RequiredArgsConstructor
public class AutocodeClient {

    private final RestClient autocodeRestClient;

    public WantedInfoDto getWantedInfo(@NonNull String vin) {
        return autocodeRestClient.get()
                .uri("/gai/wanted/{vin}", vin)
                .retrieve()
                .body(WantedInfoDto.class);
    }

    public InspectionDto getInspectionInfo(@NonNull String vin) {
        return autocodeRestClient.get()
                .uri("/gai/inspection/{vin}", vin)
                .retrieve()
                .body(InspectionDto.class);
    }

    public AccidentInfoDto getAccidentInfo(@NonNull String vin) {
        return autocodeRestClient.get()
                .uri("/gai/accident/{vin}", vin)
                .retrieve()
                .body(AccidentInfoDto.class);
    }

    public AutocodeInfoResponse getAutocodeInfo(@NonNull String vin) {
        return autocodeRestClient.get()
                .uri("/gai/autocode/{vin}", vin)
                .retrieve()
                .body(AutocodeInfoResponse.class);
    }

}
