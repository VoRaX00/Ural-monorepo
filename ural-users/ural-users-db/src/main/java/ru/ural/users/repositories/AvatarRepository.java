package ru.ural.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ural.users.entities.Avatar;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
}
