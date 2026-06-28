package ru.ural.users.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ural.entities.BaseEntity;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "avatars")
public class Avatar extends BaseEntity {

    @Column(nullable = false)
    private Long photoId;

    @Column(nullable = false)
    private Long photoThumbnailId;

    @Column(name = "crop_x", nullable = false)
    private Integer cropX;

    @Column(name = "crop_y", nullable = false)
    private Integer cropY;

    @Column(nullable = false)
    private Integer cropSize;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid", referencedColumnName = "uuid", nullable = false, unique = true)
    private User user;

}
