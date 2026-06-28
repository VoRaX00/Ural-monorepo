package ru.ural.ai.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DownloadedImage {

    private String mimeType;

    private String base64Data;

}
