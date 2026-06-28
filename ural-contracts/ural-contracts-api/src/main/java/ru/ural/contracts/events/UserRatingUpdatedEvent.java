package ru.ural.contracts.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRatingUpdatedEvent {

    private String userUuid;

    private BigDecimal averageRating;

    private Long ratingsCount;

}
