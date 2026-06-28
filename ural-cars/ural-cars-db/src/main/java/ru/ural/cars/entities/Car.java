package ru.ural.cars.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.ural.cars.enums.BodyType;
import ru.ural.cars.enums.CarType;
import ru.ural.cars.enums.CarConditionStatus;
import ru.ural.cars.enums.LoadingType;
import ru.ural.entities.BaseEntity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cars")
public class Car extends BaseEntity {

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CarType carType;

    @Column(nullable = false)
    private String carName;

    @Column(nullable = false)
    private String carModel;

    @Builder.Default
    @ElementCollection(targetClass = BodyType.class)
    @CollectionTable(name = "car_body_types", joinColumns = @JoinColumn(name = "car_id"))
    @Column(name = "body_type_code", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<BodyType> bodyType = new ArrayList<>();

    @Builder.Default
    @ElementCollection(targetClass = LoadingType.class)
    @CollectionTable(name = "car_loading_types", joinColumns = @JoinColumn(name = "car_id"))
    @Column(name = "loading_type_code", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<LoadingType> loadingType = new ArrayList<>();

    @Column(nullable = false)
    private Integer yearProduction;

    @Column(nullable = false)
    private String userUuid;

    @Column(nullable = false)
    private BigDecimal loadCapacity;

    @JdbcTypeCode(SqlTypes.JSON)
    private Address departurePlace;

    @JdbcTypeCode(SqlTypes.JSON)
    private Address destinationPlace;

    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @Column
    private ZonedDateTime updatedAt;

    @Column(nullable = false, unique = true)
    private String vinNumber;

    @Builder.Default
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Long> fileIds = new ArrayList<>();

    @Column
    private String photoAnalysisSummary;

    @Column
    @Enumerated(value = EnumType.STRING)
    private CarConditionStatus photoAnalysisStatus;

    @Builder.Default
    @Column(nullable = false)
    private Boolean booked = false;

    @OneToOne(mappedBy = "car", fetch = FetchType.LAZY)
    private GibddInfo gibddInfo;

}
