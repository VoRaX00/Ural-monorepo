package ru.ural.cars.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Элемент справочника")
public class DictionaryItemDto {

    @Schema(description = "Код")
    private String code;

    @Schema(description = "Отображаемое название")
    private String label;

}
