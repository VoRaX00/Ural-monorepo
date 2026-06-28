package ru.ural.cars.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Справочники транспорта")
public class CarDictionariesDto {

    @Schema(description = "Допустимые типы кузова")
    private List<DictionaryItemDto> bodyTypes;

    @Schema(description = "Допустимые типы загрузки")
    private List<DictionaryItemDto> loadingTypes;

}
