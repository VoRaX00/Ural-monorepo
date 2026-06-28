package ru.ural.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import ru.ural.dto.InspectionDto;
import ru.ural.dto.accidents.AccidentDto;
import ru.ural.dto.accidents.AccidentInfoDto;
import ru.ural.dto.autocode.AutocodeInfoDto;
import ru.ural.dto.wanted.WantedDto;
import ru.ural.dto.wanted.WantedInfoDto;
import ru.ural.properties.AccidentProperty;
import ru.ural.properties.InspectionProperty;
import ru.ural.properties.WantedProperty;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class GaiService {

    private static final String WANTED_DESCRIPTION_PATTERN = "Пропала машина с вин-номером: %s";

    private static final String ACCIDENT_DESCRIPTION_PATTERN = "Машина с вин-номером: %s была участником аварии";

    private static final String INSPECTION_DESCRIPTION_PATTERN = "Тех. осмотр для машины: %s - проведен";

    private final WantedProperty wantedProperty;

    private final AccidentProperty accidentProperty;

    private final InspectionProperty inspectionProperty;

    public WantedInfoDto getWantedInfo(@NonNull String vin) {
        MockProfile profile = buildProfile(vin);
        if (isGoodMock(vin, wantedProperty.getGoodVins()) || !profile.isWanted()) {
            return WantedInfoDto.builder()
                    .isWanted(false)
                    .wantedList(List.of())
                    .build();
        }

        WantedDto wantedDto = WantedDto.builder()
                .startDate(LocalDate.now().minusDays(profile.daysOffset() + 30L))
                .endDate(LocalDate.now().minusDays(profile.daysOffset()))
                .description(WANTED_DESCRIPTION_PATTERN.formatted(vin))
                .build();

        return WantedInfoDto.builder()
                .isWanted(true)
                .wantedList(List.of(wantedDto))
                .build();
    }

    public AccidentInfoDto getAccidentInfo(@NonNull String vin) {
        MockProfile profile = buildProfile(vin);
        if (isGoodMock(vin, accidentProperty.getGoodVins()) || profile.accidentsCount() == 0) {
            return AccidentInfoDto.builder()
                    .isAccident(false)
                    .accidents(List.of())
                    .build();
        }

        return AccidentInfoDto.builder()
                .isAccident(true)
                .accidents(IntStream.range(0, profile.accidentsCount())
                        .mapToObj(index -> AccidentDto.builder()
                                .date(LocalDate.now().minusMonths(index + 1L).minusDays(profile.daysOffset()))
                                .description(ACCIDENT_DESCRIPTION_PATTERN.formatted(vin))
                                .build())
                        .toList())
                .build();
    }

    public InspectionDto getInspectionInfo(@NonNull String vin) {
        MockProfile profile = buildProfile(vin);
        boolean isGoodMock = isGoodMock(vin, inspectionProperty.getGoodVins());

        return InspectionDto.builder()
                .date(LocalDate.now().minusDays(isGoodMock ? 0 : profile.daysOffset()))
                .description(INSPECTION_DESCRIPTION_PATTERN.formatted(vin))
                .build();
    }

    public AutocodeInfoDto getAutocodeInfo(@NonNull String vin) {
        MockProfile profile = buildProfile(vin);
        WantedInfoDto wantedInfo = getWantedInfo(vin);
        AccidentInfoDto accidentInfo = getAccidentInfo(vin);
        InspectionDto inspectionInfo = getInspectionInfo(vin);
        boolean isWanted = Boolean.TRUE.equals(wantedInfo.getIsWanted());
        int accidentsCount = accidentInfo.getAccidents() == null ? 0 : accidentInfo.getAccidents().size();

        Map<String, Object> rawResponse = new LinkedHashMap<>();
        rawResponse.put("source", "ural-gai-autocode-mock");
        rawResponse.put("vin", vin);
        rawResponse.put("profileSeed", profile.seed());
        rawResponse.put("wantedInfo", wantedInfo);
        rawResponse.put("accidentInfo", accidentInfo);
        rawResponse.put("inspectionInfo", inspectionInfo);
        rawResponse.put("registrationRestrictions", profile.hasRegistrationRestrictions());
        rawResponse.put("pledged", profile.isPledged());

        return AutocodeInfoDto.builder()
                .vin(vin)
                .ownersCount(profile.ownersCount())
                .accidentsCount(accidentsCount)
                .hasRegistrationRestrictions(profile.hasRegistrationRestrictions())
                .isWanted(isWanted)
                .isPledged(profile.isPledged())
                .checkedAt(ZonedDateTime.now())
                .wantedInfo(wantedInfo)
                .accidentInfo(accidentInfo)
                .inspectionInfo(inspectionInfo)
                .rawResponse(rawResponse)
                .build();
    }

    private boolean isGoodMock(String vin, List<String> goodVins) {
        return goodVins.contains(vin);
    }

    private MockProfile buildProfile(String vin) {
        int seed = Math.floorMod(vin.hashCode(), 10_000);
        return new MockProfile(
                seed,
                Math.floorMod(seed, 5) + 1,
                Math.floorMod(seed / 7, 4),
                Math.floorMod(seed / 11, 5) == 0,
                Math.floorMod(seed / 13, 7) == 0,
                Math.floorMod(seed / 17, 6) == 0,
                Math.floorMod(seed, 60)
        );
    }

    private record MockProfile(
            int seed,
            int ownersCount,
            int accidentsCount,
            boolean hasRegistrationRestrictions,
            boolean isWanted,
            boolean isPledged,
            int daysOffset
    ) {
    }

}
