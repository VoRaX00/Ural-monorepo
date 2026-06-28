package ru.ural.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {

    private Long id;

    private String name;

    private String path;

    private String type;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private String extension;

    private Long size;

    private String userUuid;

    private String url;

}
