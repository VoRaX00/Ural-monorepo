package ru.ural.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedParamsModel {

    private Integer currentPageNumber;

    private Integer itemsOnPage;

    private String sorting;

    private String sortingValue;

    private Map<String, String> filters = new HashMap<>();

}
