package ru.ural.cars.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ural.cars.entities.Car;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {

    Optional<Car> findCarByVinNumber(String vinNumber);

}
