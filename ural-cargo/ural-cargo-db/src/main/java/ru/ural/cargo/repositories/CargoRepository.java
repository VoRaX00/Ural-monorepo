package ru.ural.cargo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ural.cargo.entities.Cargo;

public interface CargoRepository extends JpaRepository<Cargo, Long> {
}
