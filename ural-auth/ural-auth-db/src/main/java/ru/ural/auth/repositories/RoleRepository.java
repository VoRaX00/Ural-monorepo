package ru.ural.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ural.auth.entities.Role;
import ru.ural.enums.UserRole;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByCode(UserRole code);

}
