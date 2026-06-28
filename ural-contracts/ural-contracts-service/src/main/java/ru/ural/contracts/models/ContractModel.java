package ru.ural.contracts.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ural.cargo.dto.CargoDto;
import ru.ural.cars.dto.CarDto;
import ru.ural.contracts.dto.RouteDto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractModel {

    private Long id;

    private CarDto car;

    private CargoDto cargo;

    private BigDecimal price;

    private String priceRequestedByUuid;

    private ZonedDateTime priceUpdatedAt;

    private String status;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private RouteDto route;

    private String ownerUuid;

    private String relatedUserUuid;

}
