package ru.ural.ai.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarPhotoAnalysisRequestedEvent {

    private Long carId;

    private List<Long> fileIds;

    private Map<String, Object> carInfo;

    private Map<String, Object> autocodeInfo;

    private String authorization;

}
