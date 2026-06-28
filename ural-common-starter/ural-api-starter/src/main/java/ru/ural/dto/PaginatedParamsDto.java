package ru.ural.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedParamsDto {

    @Min(1)
    @Builder.Default
    @Schema(description = "Текущий номер страницы")
    private Integer currentPageNumber = 1;

    @Min(1)
    @Schema(description = "Количество элементов на странице")
    private Integer itemsOnPage = 10;

    @Builder.Default
    @Schema(description = "Поле по которому будет сортировка")
    private String sorting = "id";

    @Builder.Default
    @Schema(description = "Порядок сортировки")
    private String sortingValue = "desc";

    @Schema(description = "Фильтры")
    private Map<String, String> filters = new HashMap<>();

}
