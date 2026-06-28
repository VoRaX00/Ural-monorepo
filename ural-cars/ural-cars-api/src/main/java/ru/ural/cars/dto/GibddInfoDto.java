package ru.ural.cars.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Информация проверки транспорта в Автокод/ГИБДД")
public class GibddInfoDto {

    private Integer ownersCount;

    private Integer accidentsCount;

    private Boolean hasRegistrationRestrictions;

    private Boolean wanted;

    private Boolean pledged;

    private ZonedDateTime lastCheckAt;

    private Map<String, Object> rawResponse;

}
