package ru.ural.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ural.auth.entities.Credential;

import java.util.Optional;
import java.util.UUID;

public interface CredentialRepository extends JpaRepository<Credential, Long> {

    Optional<Credential> findByUserUuid(UUID userUuid);

}
