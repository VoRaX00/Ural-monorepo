package ru.ural.cars.clients.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutocodeInfoResponse {

    private String vin;

    private Integer ownersCount;

    private Integer accidentsCount;

    private Boolean hasRegistrationRestrictions;

    @JsonAlias({"wanted", "isWanted"})
    @JsonProperty("isWanted")
    private Boolean wanted;

    @JsonAlias({"pledged", "isPledged"})
    @JsonProperty("isPledged")
    private Boolean pledged;

    private ZonedDateTime checkedAt;

    private Map<String, Object> rawResponse;

}
