package ru.ural.dto;

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
public class PageDto<T> {

    @Schema(description = "Текущий номер станицы")
    private int currentPageNumber;

    @Schema(description = "Общее число страниц")
    private int totalPageCount;

    @Schema(description = "Общее число элементов")
    private int totalResultCount;

    @Schema(description = "Элементы для текущей страницы")
    private List<T> items;

    @Schema(description = "Число элементов на текущей странице")
    private int itemsOnPage;

}
