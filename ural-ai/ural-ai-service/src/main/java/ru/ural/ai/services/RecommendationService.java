package ru.ural.ai.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ural.ai.clients.CargoServiceClient;
import ru.ural.ai.clients.CarsServiceClient;
import ru.ural.ai.dto.RecommendedCarDto;
import ru.ural.ai.dto.RecommendedCargoDto;
import ru.ural.cargo.dto.CargoDto;
import ru.ural.cars.dto.CarDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private static final String CARGO_SEARCH_STATUS = "В поиске";

    private static final int BODY_MATCH_SCORE = 35;

    private static final int LOADING_MATCH_SCORE = 25;

    private static final int UNLOADING_MATCH_SCORE = 10;

    private static final int CAPACITY_MATCH_SCORE = 30;

    private final CargoServiceClient cargoServiceClient;

    private final CarsServiceClient carsServiceClient;

    private final ObjectMapper objectMapper;

    public List<RecommendedCarDto> getRecommendedCars(Long cargoId, String authorization) {
        CargoDto cargo = cargoServiceClient.getCargoById(cargoId, authorization);
        String currentUserUuid = getUserUuid(authorization);

        return carsServiceClient.getCarPageByUserUuid(currentUserUuid, authorization).getItems()
                .stream()
                .filter(car -> !Boolean.TRUE.equals(car.getBooked()))
                .map(car -> scoreCar(cargo, car))
                .filter(Objects::nonNull)
                .sorted(Comparator
                        .comparing(RecommendedCarDto::getScore).reversed()
                        .thenComparing(item -> capacityGap(cargo.getWeight(), item.getCar().getLoadCapacity()))
                        .thenComparing(item -> item.getCar().getId(), Comparator.reverseOrder()))
                .toList();
    }

    public List<RecommendedCargoDto> getRecommendedCargo(Long carId, String authorization) {
        CarDto car = carsServiceClient.getCarById(carId, authorization);
        String currentUserUuid = getUserUuid(authorization);

        return cargoServiceClient.getCargoPageByUserUuid(currentUserUuid, authorization).getItems()
                .stream()
                .filter(this::isSearchCargo)
                .map(cargo -> scoreCargo(car, cargo))
                .filter(Objects::nonNull)
                .sorted(Comparator
                        .comparing(RecommendedCargoDto::getScore).reversed()
                        .thenComparing(item -> capacityGap(item.getCargo().getWeight(), car.getLoadCapacity()))
                        .thenComparing(item -> item.getCargo().getId(), Comparator.reverseOrder()))
                .toList();
    }

    private RecommendedCarDto scoreCar(CargoDto cargo, CarDto car) {
        if (!isCapacityEnough(cargo.getWeight(), car.getLoadCapacity())) {
            return null;
        }
        if (!hasIntersection(cargo.getBodyTypes(), car.getBodyType())) {
            return null;
        }
        if (!hasIntersection(cargo.getLoadingTypes(), car.getLoadingType())) {
            return null;
        }

        List<String> reasons = new ArrayList<>();
        int score = CAPACITY_MATCH_SCORE + capacityEfficiencyScore(cargo.getWeight(), car.getLoadCapacity());
        reasons.add("Подходит по грузоподъемности");

        score += BODY_MATCH_SCORE;
        reasons.add("Совпадает тип кузова");

        score += LOADING_MATCH_SCORE;
        reasons.add("Совпадает тип погрузки");

        if (hasIntersection(cargo.getUnloadingTypes(), car.getLoadingType())) {
            score += UNLOADING_MATCH_SCORE;
            reasons.add("Подходит для разгрузки");
        }

        return RecommendedCarDto.builder()
                .car(car)
                .score(normalizeScore(score))
                .reasons(reasons)
                .build();
    }

    private RecommendedCargoDto scoreCargo(CarDto car, CargoDto cargo) {
        if (!isCapacityEnough(cargo.getWeight(), car.getLoadCapacity())) {
            return null;
        }
        if (!hasIntersection(cargo.getBodyTypes(), car.getBodyType())) {
            return null;
        }
        if (!hasIntersection(cargo.getLoadingTypes(), car.getLoadingType())) {
            return null;
        }

        List<String> reasons = new ArrayList<>();
        int score = CAPACITY_MATCH_SCORE + capacityEfficiencyScore(cargo.getWeight(), car.getLoadCapacity());
        reasons.add("Груз помещается по весу");

        score += BODY_MATCH_SCORE;
        reasons.add("Совпадает тип кузова");

        score += LOADING_MATCH_SCORE;
        reasons.add("Совпадает тип погрузки");

        if (hasIntersection(cargo.getUnloadingTypes(), car.getLoadingType())) {
            score += UNLOADING_MATCH_SCORE;
            reasons.add("Подходит для разгрузки");
        }

        return RecommendedCargoDto.builder()
                .cargo(cargo)
                .score(normalizeScore(score))
                .reasons(reasons)
                .build();
    }

    private int normalizeScore(int score) {
        return Math.min(100, Math.max(0, score));
    }

    private boolean isSearchCargo(CargoDto cargo) {
        String status = cargo.getStatus();
        return status == null
                || "SEARCH".equalsIgnoreCase(status)
                || CARGO_SEARCH_STATUS.equalsIgnoreCase(status);
    }

    private boolean isCapacityEnough(BigDecimal cargoWeight, BigDecimal loadCapacity) {
        return cargoWeight != null && loadCapacity != null && loadCapacity.compareTo(cargoWeight) >= 0;
    }

    private int capacityEfficiencyScore(BigDecimal cargoWeight, BigDecimal loadCapacity) {
        if (!isCapacityEnough(cargoWeight, loadCapacity) || loadCapacity.signum() == 0) {
            return 0;
        }

        BigDecimal ratio = cargoWeight.divide(loadCapacity, 4, RoundingMode.HALF_UP);
        return Math.max(0, ratio.multiply(BigDecimal.valueOf(20)).intValue());
    }

    private BigDecimal capacityGap(BigDecimal cargoWeight, BigDecimal loadCapacity) {
        if (cargoWeight == null || loadCapacity == null) {
            return BigDecimal.ZERO;
        }

        return loadCapacity.subtract(cargoWeight).abs();
    }

    private boolean hasIntersection(List<String> left, List<String> right) {
        if (left == null || right == null || left.isEmpty() || right.isEmpty()) {
            return false;
        }

        return left.stream().map(this::normalize).anyMatch(leftValue ->
                right.stream().map(this::normalize).anyMatch(leftValue::equals));
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    private String getUserUuid(String authorization) {
        if (authorization == null || authorization.isBlank()) {
            throw new IllegalArgumentException("Authorization header is required");
        }

        try {
            String token = authorization.replaceFirst("(?i)^Bearer\\s+", "");
            String payload = new String(
                    Base64.getUrlDecoder().decode(token.split("\\.")[1]),
                    StandardCharsets.UTF_8
            );
            JsonNode node = objectMapper.readTree(payload);
            JsonNode userUuid = node.path("user_uuid");
            if (userUuid.isMissingNode() || userUuid.asText().isBlank()) {
                userUuid = node.path("sub");
            }
            if (userUuid.isMissingNode() || userUuid.asText().isBlank()) {
                throw new IllegalArgumentException("User uuid is missing in token");
            }
            return userUuid.asText();
        } catch (IllegalArgumentException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new IllegalArgumentException("Invalid authorization token", exception);
        }
    }

}
