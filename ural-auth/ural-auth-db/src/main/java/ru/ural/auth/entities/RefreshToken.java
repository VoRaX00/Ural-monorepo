package ru.ural.auth.entities;

import jakarta.persistence.*;
import lombok.*;
import ru.ural.entities.BaseEntity;

import java.time.ZonedDateTime;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_tokens")
public class RefreshToken extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid")
    private User user;

    @Column(nullable = false)
    private String accessJti;

    @Column(nullable = false, unique = true)
    private String refreshJti;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private ZonedDateTime issuedAt;

    @Column(nullable = false)
    private ZonedDateTime expiredAt;

}
