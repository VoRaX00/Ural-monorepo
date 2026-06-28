package ru.ural.cars.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gibdd_info")
public class GibddInfo {

    @Id
    @Column(name = "car_id")
    private Long carId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "car_id")
    private Car car;

    @Column(name = "owners_count")
    private Integer ownersCount;

    @Column(name = "accidents_count")
    private Integer accidentsCount;

    @Column(name = "has_registration_restrictions")
    private Boolean hasRegistrationRestrictions;

    @Column(name = "is_wanted")
    private Boolean wanted;

    @Column(name = "is_pledged")
    private Boolean pledged;

    @Column(name = "last_check_at")
    private ZonedDateTime lastCheckAt;

    @Builder.Default
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "raw_response", columnDefinition = "jsonb")
    private Map<String, Object> rawResponse = new HashMap<>();

}
