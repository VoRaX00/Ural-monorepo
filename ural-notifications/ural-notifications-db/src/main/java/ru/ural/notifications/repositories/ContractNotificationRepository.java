package ru.ural.notifications.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ural.notifications.entities.ContractNotification;

import java.util.List;

public interface ContractNotificationRepository extends JpaRepository<ContractNotification, Long> {

    @Query(
            value = "SELECT * FROM ural_notifications.contract_notification cn "
                    + "WHERE jsonb_exists(cn.user_uuids, :userUuid) "
                    + "ORDER BY cn.id DESC",
            nativeQuery = true
    )
    List<ContractNotification> findAllByUserUuidsContains(String userUuid);

}
