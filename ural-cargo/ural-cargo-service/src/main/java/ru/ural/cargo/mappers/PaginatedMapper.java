package ru.ural.cargo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.ural.dto.PageDto;
import ru.ural.dto.PaginatedParamsDto;
import ru.ural.models.PageModel;
import ru.ural.models.PaginatedParamsModel;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaginatedMapper {

    PaginatedParamsModel toModel(PaginatedParamsDto paramsDto);

    default <T, R> PageDto<R> toPageDto(PageModel<T> source, Function<T, R> itemsMapper) {
        if (source == null) {
            return null;
        }

        List<R> mappedItems = Optional.ofNullable(source.getItems())
                .orElse(List.of())
                .stream()
                .map(itemsMapper)
                .toList();

        return PageDto.<R>builder()
                .currentPageNumber(source.getCurrentPageNumber())
                .itemsOnPage(source.getItemsOnPage())
                .totalPageCount(source.getTotalPageCount())
                .totalResultCount(source.getTotalResultCount())
                .items(mappedItems)
                .build();
    }

}
