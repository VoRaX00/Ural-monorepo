package ru.ural.contracts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Координата маршрута")
public class RoutePointDto {

    @Schema(description = "Широта")
    private BigDecimal latitude;

    @Schema(description = "Долгота")
    private BigDecimal longitude;

}
