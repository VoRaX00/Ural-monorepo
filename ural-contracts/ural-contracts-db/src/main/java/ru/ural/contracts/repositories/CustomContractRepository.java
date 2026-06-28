package ru.ural.contracts.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import ru.ural.contracts.entities.Contract;
import ru.ural.models.PaginatedParamsModel;
import ru.ural.repositories.AbstractFilterRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class CustomContractRepository extends AbstractFilterRepository<Contract> {

    public CustomContractRepository(EntityManager entityManager) {
        super(entityManager, Contract.class);
    }

    public List<Contract> getItems(PaginatedParamsModel params) {
        return find(params);
    }

    public int getTotalResultCount(Map<String, String> filters) {
        return count(filters);
    }

    @Override
    protected List<Predicate> getPredicates(
            CriteriaQuery<?> query,
            CriteriaBuilder builder,
            Root<Contract> root,
            Map<String, String> filters
    ) {
        List<Predicate> predicates = new ArrayList<>();

        String startDate = filters.get("startDate");
        String endDate = filters.get("endDate");
        if (startDate != null && endDate != null) {
            predicates.add(
                    builder.between(
                            root.get("startDate"),
                            builder.literal(LocalDate.parse(
                                    startDate,
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            ).atStartOfDay()),
                            builder.literal(LocalDate.parse(
                                    endDate,
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            ).atTime(LocalTime.MAX))
                    )
            );
        }

        return predicates;
    }

}
