package ru.ural.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ural.users.entities.UserRatingSummary;

import java.util.UUID;

public interface UserRatingSummaryRepository extends JpaRepository<UserRatingSummary, UUID> {
}
