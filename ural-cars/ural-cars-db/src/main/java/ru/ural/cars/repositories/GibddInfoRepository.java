package ru.ural.cars.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ural.cars.entities.GibddInfo;

public interface GibddInfoRepository extends JpaRepository<GibddInfo, Long> {
}
