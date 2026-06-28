package ru.ural.ai.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarPhotoAnalysisResult {

    private String summary;

    private String status;

}
