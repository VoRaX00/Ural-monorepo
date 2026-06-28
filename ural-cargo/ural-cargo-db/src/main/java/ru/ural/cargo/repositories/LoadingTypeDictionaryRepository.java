package ru.ural.cargo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ural.cargo.entities.LoadingTypeDictionary;

import java.util.List;

public interface LoadingTypeDictionaryRepository extends JpaRepository<LoadingTypeDictionary, String> {

    List<LoadingTypeDictionary> findAllByActiveTrueOrderBySortOrderAsc();

}
