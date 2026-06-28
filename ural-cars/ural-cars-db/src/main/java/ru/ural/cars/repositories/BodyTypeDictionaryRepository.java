package ru.ural.cars.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ural.cars.entities.BodyTypeDictionary;

import java.util.List;

public interface BodyTypeDictionaryRepository extends JpaRepository<BodyTypeDictionary, String> {

    List<BodyTypeDictionary> findAllByActiveTrueOrderBySortOrderAsc();

}
