package ru.ural.contracts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ural.contracts.entities.ContractRating;

import java.util.List;
import java.util.Optional;

public interface ContractRatingRepository extends JpaRepository<ContractRating, Long> {

    Optional<ContractRating> findByContractIdAndRaterUserUuid(Long contractId, String raterUserUuid);

    List<ContractRating> findAllByRatedUserUuidOrderByCreatedAtDesc(String ratedUserUuid);

    long countByRatedUserUuid(String ratedUserUuid);

    @Query("select coalesce(avg(r.rating), 0) from ContractRating r where r.ratedUserUuid = :ratedUserUuid")
    Double getAverageRating(String ratedUserUuid);

}
