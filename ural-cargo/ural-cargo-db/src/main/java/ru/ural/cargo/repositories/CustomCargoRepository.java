package ru.ural.cargo.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import ru.ural.cargo.entities.Cargo;
import ru.ural.cargo.enums.CargoStatus;
import ru.ural.models.PaginatedParamsModel;
import ru.ural.repositories.AbstractFilterRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Repository
public class CustomCargoRepository extends AbstractFilterRepository<Cargo> {

    public CustomCargoRepository(EntityManager entityManager) {
        super(entityManager, Cargo.class);
    }

    public List<Cargo> getItems(PaginatedParamsModel params, boolean showUnavailable) {
        return find(params, showUnavailable);
    }

    public int getTotalResultCount(Map<String, String> filters, boolean showUnavailable) {
        return count(filters, showUnavailable);
    }

    private List<Cargo> find(PaginatedParamsModel params, boolean showUnavailable) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Cargo> query = builder.createQuery(Cargo.class);
        Root<Cargo> root = query.from(Cargo.class);
        List<Predicate> predicates = getPredicates(query, builder, root, params.getFilters(), showUnavailable);
        setOrder(params, builder, query, root);

        return getEntityManager()
                .createQuery(query.select(root).where(builder.and(predicates.toArray(new Predicate[0]))))
                .setFirstResult((params.getCurrentPageNumber() - 1) * params.getItemsOnPage())
                .setMaxResults(params.getItemsOnPage())
                .getResultList();
    }

    private int count(Map<String, String> filters, boolean showUnavailable) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Cargo> root = query.from(Cargo.class);
        List<Predicate> predicates = getPredicates(query, builder, root, filters, showUnavailable);

        return getEntityManager()
                .createQuery(query.select(builder.count(root)).where(predicates.toArray(new Predicate[0])))
                .getSingleResult()
                .intValue();
    }

    @Override
    protected List<Predicate> getPredicates(
            CriteriaQuery<?> query,
            CriteriaBuilder builder,
            Root<Cargo> root,
            Map<String, String> filters
    ) {
        return getPredicates(query, builder, root, filters, true);
    }

    private List<Predicate> getPredicates(
            CriteriaQuery<?> query,
            CriteriaBuilder builder,
            Root<Cargo> root,
            Map<String, String> filters,
            boolean showUnavailable
    ) {
        List<Predicate> predicates = new ArrayList<>();
        if (!showUnavailable) {
            predicates.add(builder.equal(root.get("status"), CargoStatus.SEARCH));
        }

        if (filters == null || filters.isEmpty()) {
            return predicates;
        }

        addDateRangePredicate(builder, root, predicates, filters);
        addLikePredicate(builder, root, predicates, filters, "name");
        addLikePredicate(builder, root, predicates, filters, "comment");
        addEqualPredicate(builder, root, predicates, filters, "userUuid");
        addStatusPredicate(builder, root, predicates, filters);

        addBigDecimalPredicates(builder, root, predicates, filters, "length");
        addBigDecimalPredicates(builder, root, predicates, filters, "width");
        addBigDecimalPredicates(builder, root, predicates, filters, "height");
        addBigDecimalPredicates(builder, root, predicates, filters, "weight");
        addBigDecimalPredicates(builder, root, predicates, filters, "price");

        addAddressPredicate(builder, root, predicates, filters, "loadingPlace", "country");
        addAddressPredicate(builder, root, predicates, filters, "loadingPlace", "city", "loadingCity");
        addAddressPredicate(builder, root, predicates, filters, "loadingPlace", "street");
        addAddressPredicate(builder, root, predicates, filters, "loadingPlace", "house");
        addAddressPredicate(builder, root, predicates, filters, "loadingPlace", "apartment");
        addAddressPredicate(builder, root, predicates, filters, "loadingPlace", "postalCode");

        addAddressPredicate(builder, root, predicates, filters, "unloadingPlace", "country");
        addAddressPredicate(builder, root, predicates, filters, "unloadingPlace", "city", "unloadingCity");
        addAddressPredicate(builder, root, predicates, filters, "unloadingPlace", "street");
        addAddressPredicate(builder, root, predicates, filters, "unloadingPlace", "house");
        addAddressPredicate(builder, root, predicates, filters, "unloadingPlace", "apartment");
        addAddressPredicate(builder, root, predicates, filters, "unloadingPlace", "postalCode");

        return predicates;
    }

    private void addDateRangePredicate(
            CriteriaBuilder builder,
            Root<Cargo> root,
            List<Predicate> predicates,
            Map<String, String> filters
    ) {
        String startDate = getFilterValue(filters, "startDate", "createdAtFrom");
        String endDate = getFilterValue(filters, "endDate", "createdAtTo");
        if (startDate == null && endDate == null) {
            return;
        }

        if (startDate != null) {
            predicates.add(builder.greaterThanOrEqualTo(
                    root.get("createdAt"),
                    parseStartOfDay(startDate)
            ));
        }
        if (endDate != null) {
            predicates.add(builder.lessThanOrEqualTo(
                    root.get("createdAt"),
                    parseEndOfDay(endDate)
            ));
        }
    }

    private void addLikePredicate(
            CriteriaBuilder builder,
            Root<Cargo> root,
            List<Predicate> predicates,
            Map<String, String> filters,
            String fieldName
    ) {
        String value = getFilterValue(filters, fieldName);
        if (value == null) {
            return;
        }

        predicates.add(builder.like(
                builder.lower(root.get(fieldName)),
                "%" + value.toLowerCase(Locale.ROOT) + "%"
        ));
    }

    private void addEqualPredicate(
            CriteriaBuilder builder,
            Root<Cargo> root,
            List<Predicate> predicates,
            Map<String, String> filters,
            String fieldName
    ) {
        String value = getFilterValue(filters, fieldName);
        if (value == null) {
            return;
        }

        predicates.add(builder.equal(root.get(fieldName), value));
    }

    private void addStatusPredicate(
            CriteriaBuilder builder,
            Root<Cargo> root,
            List<Predicate> predicates,
            Map<String, String> filters
    ) {
        String value = getFilterValue(filters, "status");
        if (value == null) {
            return;
        }

        predicates.add(builder.equal(root.get("status"), CargoStatus.parse(value)));
    }

    private void addBigDecimalPredicates(
            CriteriaBuilder builder,
            Root<Cargo> root,
            List<Predicate> predicates,
            Map<String, String> filters,
            String fieldName
    ) {
        String value = getFilterValue(filters, fieldName);
        if (value != null) {
            predicates.add(builder.equal(root.get(fieldName), new BigDecimal(value)));
        }

        String minValue = getFilterValue(filters, fieldName + "From", "min" + capitalize(fieldName));
        if (minValue != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get(fieldName), new BigDecimal(minValue)));
        }

        String maxValue = getFilterValue(filters, fieldName + "To", "max" + capitalize(fieldName));
        if (maxValue != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get(fieldName), new BigDecimal(maxValue)));
        }
    }

    private void addAddressPredicate(
            CriteriaBuilder builder,
            Root<Cargo> root,
            List<Predicate> predicates,
            Map<String, String> filters,
            String addressFieldName,
            String addressPropertyName
    ) {
        addAddressPredicate(builder, root, predicates, filters, addressFieldName, addressPropertyName, null);
    }

    private void addAddressPredicate(
            CriteriaBuilder builder,
            Root<Cargo> root,
            List<Predicate> predicates,
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

    private ZonedDateTime parseStartOfDay(String value) {
        return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE)
                .atStartOfDay(ZoneId.systemDefault());
    }

    private ZonedDateTime parseEndOfDay(String value) {
        return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE)
                .atTime(LocalTime.MAX)
                .atZone(ZoneId.systemDefault());
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
