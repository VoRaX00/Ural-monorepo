package ru.ural.cargo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ural.cargo.dto.CargoDictionariesDto;
import ru.ural.cargo.dto.DictionaryItemDto;
import ru.ural.cargo.entities.BodyTypeDictionary;
import ru.ural.cargo.entities.LoadingTypeDictionary;
import ru.ural.cargo.repositories.BodyTypeDictionaryRepository;
import ru.ural.cargo.repositories.LoadingTypeDictionaryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CargoDictionaryService {

    private final BodyTypeDictionaryRepository bodyTypeDictionaryRepository;

    private final LoadingTypeDictionaryRepository loadingTypeDictionaryRepository;

    public CargoDictionariesDto getDictionaries() {
        List<DictionaryItemDto> bodyTypes = bodyTypeDictionaryRepository.findAllByActiveTrueOrderBySortOrderAsc()
                .stream()
                .map(this::toDto)
                .toList();
        List<DictionaryItemDto> loadingTypes = loadingTypeDictionaryRepository.findAllByActiveTrueOrderBySortOrderAsc()
                .stream()
                .map(this::toDto)
                .toList();

        return CargoDictionariesDto.builder()
                .bodyTypes(bodyTypes)
                .loadingTypes(loadingTypes)
                .unloadingTypes(loadingTypes)
                .build();
    }

    private DictionaryItemDto toDto(BodyTypeDictionary dictionary) {
        return DictionaryItemDto.builder()
                .code(dictionary.getCode())
                .label(dictionary.getLabel())
                .build();
    }

    private DictionaryItemDto toDto(LoadingTypeDictionary dictionary) {
        return DictionaryItemDto.builder()
                .code(dictionary.getCode())
                .label(dictionary.getLabel())
                .build();
    }

}
