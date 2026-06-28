package ru.ural.cars.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ural.dto.AddressDto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {

    @Schema(description = "Id автомобиля")
    private Long id;

    @Schema(description = "Тип автомобиля")
    private String carType;

    @Schema(description = "Наименование автомобиля")
    private String carName;

    @Schema(description = "Модель автомобиля")
    private String carModel;

    @Schema(description = "Грузоподъемность")
    private BigDecimal loadCapacity;

    @Schema(description = "Планируемое место отправления транспорта")
    private AddressDto departurePlace;

    @Schema(description = "Планируемое место прибытия транспорта")
    private AddressDto destinationPlace;

    @Schema(description = "Год производства")
    private Integer yearProduction;

    @Schema(description = "Uuid пользователя")
    private String userUuid;

    @Schema(description = "Дата создания")
    private ZonedDateTime createdAt;

    @Schema(description = "Дата обновления")
    private ZonedDateTime updatedAt;

    @Schema(description = "ВИН-номер")
    private String vinNumber;

    @Schema(description = "Id файлов транспортного средства")
    private List<Long> fileIds;

    @Schema(description = "Тип кузова")
    private List<String> bodyType;

    @Schema(description = "Тип загрузки")
    private List<String> loadingType;

    @Schema(description = "Краткое описание состояния по фотографиям")
    private String photoAnalysisSummary;

    @Schema(description = "Статус состояния по фотографиям")
    private String photoAnalysisStatus;

    @Schema(description = "Транспорт занят контрактом")
    private Boolean booked;

    @Schema(description = "Информация проверки транспорта в Автокод/ГИБДД")
    private GibddInfoDto gibddInfo;

}
