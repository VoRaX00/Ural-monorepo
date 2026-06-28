package ru.ural.ai.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarPhotoAnalysisCompletedEvent {

    private Long carId;

    private String summary;

    private String status;

    private Boolean success;

}
