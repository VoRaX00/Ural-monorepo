package ru.ural.contracts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ural.cargo.dto.CargoDto;
import ru.ural.cars.dto.CarDto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto контракта")
public class ContractDto {

    @Schema(description = "Id контракта")
    private Long id;

    @Schema(description = "Id машины")
    private CarDto car;

    @Schema(description = "Id груза")
    private CargoDto cargo;

    @Schema(description = "Статус")
    private String status;

    @Schema(description = "Стоимость")
    private BigDecimal price;

    @Schema(description = "Пользователь, предложивший текущую стоимость")
    private String priceRequestedByUuid;

    @Schema(description = "Дата обновления стоимости")
    private ZonedDateTime priceUpdatedAt;

    @Schema(description = "Дата создания")
    private ZonedDateTime createdAt;

    @Schema(description = "Дата обновления")
    private ZonedDateTime updatedAt;

    @Schema(description = "Маршрут перевозки")
    private RouteDto route;

    @Schema(description = "Пользователь, создавший контракт")
    private String ownerUuid;

    @Schema(description = "Пользователь, которому предложили контракт")
    private String relatedUserUuid;

}
