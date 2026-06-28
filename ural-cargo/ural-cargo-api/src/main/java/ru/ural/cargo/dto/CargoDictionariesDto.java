package ru.ural.cargo.dto;

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
@Schema(description = "Справочники грузов")
public class CargoDictionariesDto {

    @Schema(description = "Допустимые типы кузова")
    private List<DictionaryItemDto> bodyTypes;

    @Schema(description = "Допустимые типы погрузки")
    private List<DictionaryItemDto> loadingTypes;

    @Schema(description = "Допустимые типы разгрузки")
    private List<DictionaryItemDto> unloadingTypes;

}
