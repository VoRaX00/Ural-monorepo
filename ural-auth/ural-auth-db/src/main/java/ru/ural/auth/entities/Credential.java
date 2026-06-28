package ru.ural.auth.entities;

import jakarta.persistence.*;
import lombok.*;
import ru.ural.entities.BaseEntity;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "credentials")
public class Credential extends BaseEntity {

    @Column(nullable = false)
    private String salt;

    @Column(nullable = false)
    private String hash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid")
    private User user;

}
