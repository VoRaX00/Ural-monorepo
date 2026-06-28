package ru.ural.contracts.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ural.contracts.enums.ContractStatus;
import ru.ural.entities.BaseEntity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contracts")
public class Contract extends BaseEntity {

    @Column(nullable = false)
    private Long carId;

    @Column(nullable = false)
    private Long cargoId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ContractStatus status;

    @Column
    private BigDecimal price;

    @Column(nullable = false)
    private String priceRequestedByUuid;

    @Column
    private ZonedDateTime priceUpdatedAt;

    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @Column
    private ZonedDateTime updatedAt;

    @Column(nullable = false)
    private String ownerUuid;

    @Column(nullable = false)
    private String relatedUserUuid;

    @OneToOne(mappedBy = "contract", fetch = FetchType.LAZY)
    private Route route;

}
