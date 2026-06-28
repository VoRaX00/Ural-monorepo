package ru.ural.notifications.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.ural.entities.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contract_notification", schema = "ural_notifications")
public class ContractNotification extends BaseEntity {

    @Column(nullable = false)
    private Long contractId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    @Builder.Default
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    private List<String> userUuids = new ArrayList<>();

    @Builder.Default
    @Column(nullable = false)
    private Boolean isRead = false;

}
