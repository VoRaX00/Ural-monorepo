package ru.ural.cars.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ural.dto.AddressDto;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarRequest {

    @Schema(description = "Тип автомобиля")
    private String carType;

    @Schema(description = "Наименование автомобиля")
    private String carName;

    @Schema(description = "Модель автомобиля")
    private String carModel;

    @Schema(description = "Тип кузова")
    private List<String> bodyType;

    @Schema(description = "Тип загрузки")
    private List<String> loadingType;

    @Schema(description = "Грузоподъемность")
    private BigDecimal loadCapacity;

    @Schema(description = "Планируемое место отправления транспорта")
    private AddressDto departurePlace;

    @Schema(description = "Планируемое место прибытия транспорта")
    private AddressDto destinationPlace;

    @Schema(description = "Год производства")
    private Integer yearProduction;

    @Schema(description = "ВИН-номер")
    private String vinNumber;

    @Schema(description = "Id файлов транспортного средства")
    private List<Long> fileIds;

}
