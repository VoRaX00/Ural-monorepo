package ru.ural.contracts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ural.contracts.entities.Contract;
import ru.ural.contracts.enums.ContractStatus;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {

    @Query("""
            SELECT c
            FROM Contract c
            WHERE c.status = :status
              AND (c.ownerUuid = :userUuid OR c.relatedUserUuid = :userUuid)
            ORDER BY COALESCE(c.updatedAt, c.createdAt) DESC
            """)
    List<Contract> findCompletedByUser(ContractStatus status, String userUuid);
}
