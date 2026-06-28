package ru.ural.cars.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ural.cars.dto.CarDictionariesDto;
import ru.ural.cars.dto.DictionaryItemDto;
import ru.ural.cars.entities.BodyTypeDictionary;
import ru.ural.cars.entities.LoadingTypeDictionary;
import ru.ural.cars.repositories.BodyTypeDictionaryRepository;
import ru.ural.cars.repositories.LoadingTypeDictionaryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarDictionaryService {

    private final BodyTypeDictionaryRepository bodyTypeDictionaryRepository;

    private final LoadingTypeDictionaryRepository loadingTypeDictionaryRepository;

    public CarDictionariesDto getDictionaries() {
        return CarDictionariesDto.builder()
                .bodyTypes(bodyTypeDictionaryRepository.findAllByActiveTrueOrderBySortOrderAsc()
                        .stream()
                        .map(this::toDto)
                        .toList())
                .loadingTypes(loadingTypeDictionaryRepository.findAllByActiveTrueOrderBySortOrderAsc()
                        .stream()
                        .map(this::toDto)
                        .toList())
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
