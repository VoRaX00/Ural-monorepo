package ru.ural.users.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ural.users.dto.UserRatingUpdatedEvent;
import ru.ural.users.entities.UserRatingSummary;
import ru.ural.users.repositories.UserRatingSummaryRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserRatingService {

    private static final BigDecimal DEFAULT_RATING = BigDecimal.valueOf(5).setScale(2, RoundingMode.HALF_UP);

    private final UserRatingSummaryRepository userRatingSummaryRepository;

    @Transactional
    public void updateRating(UserRatingUpdatedEvent event) {
        UUID userUuid = UUID.fromString(event.getUserUuid());
        UserRatingSummary summary = userRatingSummaryRepository.findById(userUuid)
                .orElseGet(() -> UserRatingSummary.builder()
                        .userUuid(userUuid)
                        .averageRating(DEFAULT_RATING)
                        .ratingsCount(0L)
                        .updatedAt(ZonedDateTime.now())
                        .build());

        summary.setAverageRating(normalizeRating(event.getAverageRating()));
        summary.setRatingsCount(event.getRatingsCount() == null ? 0L : event.getRatingsCount());
        summary.setUpdatedAt(ZonedDateTime.now());

        userRatingSummaryRepository.save(summary);
    }

    @Transactional
    public void createDefaultRating(UUID userUuid) {
        if (userRatingSummaryRepository.existsById(userUuid)) {
            return;
        }

        userRatingSummaryRepository.save(UserRatingSummary.builder()
                .userUuid(userUuid)
                .averageRating(DEFAULT_RATING)
                .ratingsCount(0L)
                .updatedAt(ZonedDateTime.now())
                .build());
    }

    public UserRatingSummary getOrDefault(UUID userUuid) {
        return userRatingSummaryRepository.findById(userUuid)
                .orElseGet(() -> UserRatingSummary.builder()
                        .userUuid(userUuid)
                        .averageRating(DEFAULT_RATING)
                        .ratingsCount(0L)
                        .updatedAt(ZonedDateTime.now())
                        .build());
    }

    private BigDecimal normalizeRating(BigDecimal rating) {
        return rating == null
                ? DEFAULT_RATING
                : rating.setScale(2, RoundingMode.HALF_UP);
    }

}
