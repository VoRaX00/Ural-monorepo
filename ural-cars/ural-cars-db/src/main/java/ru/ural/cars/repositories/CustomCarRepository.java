package ru.ural.cars.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import ru.ural.cars.entities.Car;
import ru.ural.cars.enums.CarType;
import ru.ural.models.PaginatedParamsModel;
import ru.ural.repositories.AbstractFilterRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Repository
public class CustomCarRepository extends AbstractFilterRepository<Car> {

    public CustomCarRepository(EntityManager entityManager) {
        super(entityManager, Car.class);
    }

    public List<Car> getItems(PaginatedParamsModel params) {
        return find(params);
    }

    public List<Car> getItems(PaginatedParamsModel params, boolean showBooked) {
        return find(params, showBooked);
    }

    public int getTotalResultCount(Map<String, String> filters) {
        return count(filters);
    }

    public int getTotalResultCount(Map<String, String> filters, boolean showBooked) {
        return count(filters, showBooked);
    }

    private List<Car> find(PaginatedParamsModel params, boolean showBooked) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Car> query = builder.createQuery(Car.class);
        Root<Car> root = query.from(Car.class);
        List<Predicate> predicates = getPredicates(query, builder, root, params.getFilters(), showBooked);
        setOrder(params, builder, query, root);

        return getEntityManager()
                .createQuery(query.select(root).where(builder.and(predicates.toArray(new Predicate[0]))))
                .setFirstResult((params.getCurrentPageNumber() - 1) * params.getItemsOnPage())
                .setMaxResults(params.getItemsOnPage())
                .getResultList();
    }

    private int count(Map<String, String> filters, boolean showBooked) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Car> root = query.from(Car.class);
        List<Predicate> predicates = getPredicates(query, builder, root, filters, showBooked);

        return getEntityManager()
                .createQuery(query.select(builder.count(root)).where(predicates.toArray(new Predicate[0])))
                .getSingleResult()
                .intValue();
    }

    @Override
    protected List<Predicate> getPredicates(
            CriteriaQuery<?> query,
            CriteriaBuilder builder,
            Root<Car> root,
            Map<String, String> filters
    ) {
        return getPredicates(query, builder, root, filters, true);
    }

    private List<Predicate> getPredicates(
            CriteriaQuery<?> query,
            CriteriaBuilder builder,
            Root<Car> root,
            Map<String, String> filters,
            boolean showBooked
    ) {
        List<Predicate> predicates = new ArrayList<>();
        if (!showBooked) {
            predicates.add(builder.isFalse(root.get("booked")));
        }

        if (filters == null) {
            return predicates;
        }

        addLikePredicate(predicates, builder, root, filters, "carName");
        addLikePredicate(predicates, builder, root, filters, "carModel");
        addLikePredicate(predicates, builder, root, filters, "vinNumber");
        addEqualPredicate(predicates, builder, root, filters, "userUuid");
        addYearProductionPredicate(predicates, builder, root, filters);
        addCarTypePredicate(predicates, builder, root, filters);
        addAddressPredicate(predicates, builder, root, filters, "departurePlace", "city", "departureCity");
        addAddressPredicate(predicates, builder, root, filters, "destinationPlace", "city", "destinationCity");

        return predicates;
    }

    private void addLikePredicate(
            List<Predicate> predicates,
            CriteriaBuilder builder,
            Root<Car> root,
            Map<String, String> filters,
            String fieldName
    ) {
        String value = getFilterValue(filters, fieldName);
        if (value != null) {
            predicates.add(
                    builder.like(
                            builder.lower(root.get(fieldName)),
                            "%" + value.toLowerCase(Locale.ROOT) + "%"
                    )
            );
        }
    }

    private void addEqualPredicate(
            List<Predicate> predicates,
            CriteriaBuilder builder,
            Root<Car> root,
            Map<String, String> filters,
            String fieldName
    ) {
        String value = getFilterValue(filters, fieldName);
        if (value != null) {
            predicates.add(builder.equal(root.get(fieldName), value));
        }
    }

    private void addYearProductionPredicate(
            List<Predicate> predicates,
            CriteriaBuilder builder,
            Root<Car> root,
            Map<String, String> filters
    ) {
        String yearProduction = getFilterValue(filters, "yearProduction");
        if (yearProduction != null) {
            predicates.add(builder.equal(root.get("yearProduction"), Integer.parseInt(yearProduction)));
        }
    }

    private void addCarTypePredicate(
            List<Predicate> predicates,
            CriteriaBuilder builder,
            Root<Car> root,
            Map<String, String> filters
    ) {
        String carType = getFilterValue(filters, "carType");
        if (carType != null) {
            CarType parsedCarType = parseCarType(carType);
            predicates.add(parsedCarType == null
                    ? builder.disjunction()
                    : builder.equal(root.get("carType"), parsedCarType));
        }
    }

    private void addAddressPredicate(
            List<Predicate> predicates,
            CriteriaBuilder builder,
            Root<Car> root,
            Map<String, String> filters,
            String addressFieldName,
            String addressPropertyName,
            String alias
    ) {
        String value = getFilterValue(
                filters,
                addressFieldName + "." + addressPropertyName,
                addressFieldName + capitalize(addressPropertyName),
                alias
        );
        if (value == null) {
            return;
        }

        var addressProperty = builder.function(
                "jsonb_extract_path_text",
                String.class,
                root.get(addressFieldName),
                builder.literal(addressPropertyName)
        );

        predicates.add(builder.like(
                builder.lower(addressProperty),
                "%" + value.toLowerCase(Locale.ROOT) + "%"
        ));
    }

    private CarType parseCarType(String value) {
        CarType carType = CarType.parse(value);
        if (carType != null) {
            return carType;
        }

        try {
            return CarType.valueOf(value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    private String getFilterValue(Map<String, String> filters, String... names) {
        for (String name : names) {
            if (name == null) {
                continue;
            }
            String value = filters.get(name);
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }

        return null;
    }

    private String capitalize(String value) {
        return value.substring(0, 1).toUpperCase(Locale.ROOT) + value.substring(1);
    }

}
