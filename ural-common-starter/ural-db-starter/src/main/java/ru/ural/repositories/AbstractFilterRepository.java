package ru.ural.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.ural.models.PaginatedParamsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public abstract class AbstractFilterRepository<T> {

    private static final String DEFAULT_SORTING_FIELD = "id";

    @Getter
    @PersistenceContext
    private final EntityManager entityManager;

    @Getter
    private final Class<T> domain;

    public int count(Map<String, String> filters) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<T> root = query.from(domain);
        List<Predicate> predicates = getPredicates(query, cb, root, filters);

        return entityManager
                .createQuery(query.select(cb.count(root)).where(predicates.toArray(new Predicate[0])))
                .getSingleResult()
                .intValue();
    }

    public List<T> find(PaginatedParamsModel params) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(domain);
        Root<T> root = query.from(domain);
        List<Predicate> predicates = getPredicates(query, criteriaBuilder, root, params.getFilters());
        setOrder(params, criteriaBuilder, query, root);

        TypedQuery<T> tq = entityManager.createQuery(query
                .select(root)
                .where(criteriaBuilder.and(predicates.toArray(new Predicate[0]))));

        return tq.setFirstResult((params.getCurrentPageNumber() - 1) * params.getItemsOnPage())
                .setMaxResults(params.getItemsOnPage())
                .getResultList();
    }

    public void setOrder(PaginatedParamsModel params, CriteriaBuilder builder, CriteriaQuery<T> query, Root<T> root) {
        String sorting = params.getSorting();
        String sortingValue = params.getSortingValue();
        List<Order> orders = new ArrayList<>();
        if (sorting != null && sortingValue != null) {
            Order order = sortingValue.equalsIgnoreCase("desc")
                    ? builder.desc(root.get(sorting))
                    : builder.asc(root.get(sorting));
            orders.add(order);
        }

        boolean isNotDefaultSortingField = DEFAULT_SORTING_FIELD.equalsIgnoreCase(sorting);
        if (!isNotDefaultSortingField) {
            orders.add(builder.desc(root.get(DEFAULT_SORTING_FIELD)));
        }

        query.orderBy(orders);
    }

    protected abstract List<Predicate> getPredicates(
            CriteriaQuery<?> query,
            CriteriaBuilder builder,
            Root<T> root,
            Map<String, String> filters
    );

}
