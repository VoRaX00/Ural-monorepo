package ru.ural.cars.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ural.cars.clients.AutocodeClient;
import ru.ural.cars.clients.dto.AutocodeInfoResponse;
import ru.ural.cars.entities.Car;
import ru.ural.cars.entities.GibddInfo;
import ru.ural.cars.repositories.GibddInfoRepository;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GibddInfoService {

    private final AutocodeClient autocodeClient;

    private final GibddInfoRepository gibddInfoRepository;

    public GibddInfo refreshInfo(Car car) {
        try {
            return refreshInfoOrThrow(car);
        } catch (RuntimeException exception) {
            log.warn("Failed to check car in Autocode/GIBDD by vin: {}", car.getVinNumber(), exception);
            return car.getGibddInfo();
        }
    }

    public GibddInfo refreshInfoOrThrow(Car car) {
        GibddInfo info = check(car);
        GibddInfo savedInfo = gibddInfoRepository.save(info);
        car.setGibddInfo(savedInfo);
        return savedInfo;
    }

    private GibddInfo check(Car car) {
        AutocodeInfoResponse autocodeInfo = autocodeClient.getAutocodeInfo(car.getVinNumber());
        Map<String, Object> rawResponse = buildRawResponse(autocodeInfo);

        GibddInfo info = gibddInfoRepository.findById(car.getId())
                .orElseGet(() -> GibddInfo.builder()
                        .car(car)
                        .build());

        info.setCar(car);
        info.setOwnersCount(autocodeInfo == null ? null : autocodeInfo.getOwnersCount());
        info.setAccidentsCount(autocodeInfo == null ? null : autocodeInfo.getAccidentsCount());
        info.setHasRegistrationRestrictions(autocodeInfo == null ? null : autocodeInfo.getHasRegistrationRestrictions());
        info.setWanted(autocodeInfo == null ? null : autocodeInfo.getWanted());
        info.setPledged(autocodeInfo == null ? null : autocodeInfo.getPledged());
        info.setLastCheckAt(autocodeInfo == null || autocodeInfo.getCheckedAt() == null
                ? ZonedDateTime.now()
                : autocodeInfo.getCheckedAt());
        info.setRawResponse(rawResponse);
        return info;
    }

    private Map<String, Object> buildRawResponse(AutocodeInfoResponse autocodeInfo) {
        if (autocodeInfo == null) {
            return Map.of();
        }

        Map<String, Object> rawResponse = new LinkedHashMap<>();
        rawResponse.put("vin", autocodeInfo.getVin());
        rawResponse.put("ownersCount", autocodeInfo.getOwnersCount());
        rawResponse.put("accidentsCount", autocodeInfo.getAccidentsCount());
        rawResponse.put("hasRegistrationRestrictions", autocodeInfo.getHasRegistrationRestrictions());
        rawResponse.put("isWanted", autocodeInfo.getWanted());
        rawResponse.put("isPledged", autocodeInfo.getPledged());
        rawResponse.put("checkedAt", autocodeInfo.getCheckedAt());
        rawResponse.put("rawResponse", autocodeInfo.getRawResponse());
        return rawResponse;
    }

}
