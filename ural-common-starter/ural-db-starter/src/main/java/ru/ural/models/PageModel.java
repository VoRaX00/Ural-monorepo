package ru.ural.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageModel<T> {

    private int currentPageNumber;

    private int totalPageCount;

    private int totalResultCount;

    private List<T> items;

    private int itemsOnPage;

}
