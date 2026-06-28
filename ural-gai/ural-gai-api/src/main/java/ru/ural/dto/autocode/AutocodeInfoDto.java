package ru.ural.dto.autocode;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ural.dto.InspectionDto;
import ru.ural.dto.accidents.AccidentInfoDto;
import ru.ural.dto.wanted.WantedInfoDto;

import java.time.ZonedDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutocodeInfoDto {

    private String vin;

    private Integer ownersCount;

    private Integer accidentsCount;

    private Boolean hasRegistrationRestrictions;

    @JsonProperty("isWanted")
    private Boolean isWanted;

    @JsonProperty("isPledged")
    private Boolean isPledged;

    private ZonedDateTime checkedAt;

    private WantedInfoDto wantedInfo;

    private AccidentInfoDto accidentInfo;

    private InspectionDto inspectionInfo;

    private Map<String, Object> rawResponse;

}
