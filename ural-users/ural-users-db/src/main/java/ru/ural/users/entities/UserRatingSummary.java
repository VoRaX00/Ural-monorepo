package ru.ural.users.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_rating_summary")
public class UserRatingSummary {

    @Id
    private UUID userUuid;

    @Column(nullable = false)
    private BigDecimal averageRating;

    @Column(nullable = false)
    private Long ratingsCount;

    @Column(nullable = false)
    private ZonedDateTime updatedAt;

}
