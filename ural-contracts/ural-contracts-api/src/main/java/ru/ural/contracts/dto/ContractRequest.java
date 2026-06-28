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
public class ContractRequest {

    @Schema(description = "Id машины")
    private Long carId;

    @Schema(description = "Id груза")
    private Long cargoId;

    @Schema(description = "Стоимость")
    private BigDecimal price;

}
