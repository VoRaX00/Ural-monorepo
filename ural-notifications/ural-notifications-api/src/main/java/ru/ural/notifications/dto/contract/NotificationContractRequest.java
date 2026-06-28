package ru.ural.notifications.dto.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationContractRequest {

    private Long contractId;

    private String title;

    private String body;

    private List<String> userUuids;

}
