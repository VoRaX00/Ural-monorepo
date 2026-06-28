package ru.ural.contracts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Маршрут контракта")
public class RouteDto {

    @Schema(description = "Id маршрута")
    private Long id;

    @Schema(description = "Id контракта")
    private Long contractId;

    @Schema(description = "Координаты маршрута")
    private List<RoutePointDto> points;

    @Schema(description = "Дистанция, м")
    private BigDecimal distanceMeters;

    @Schema(description = "Время в пути, мс")
    private Long timeMs;

    @Schema(description = "Дата формирования")
    private ZonedDateTime createdAt;

    @Schema(description = "Дата обновления")
    private ZonedDateTime updatedAt;

}
