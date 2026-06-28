package ru.ural.contracts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ural.contracts.entities.Route;

import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {

    Optional<Route> findByContractId(Long contractId);

}
