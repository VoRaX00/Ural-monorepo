package ru.ural.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {

    @Schema(description = "Сообщение об ошибке")
    private String message;

    @Schema(description = "Время ошибки")
    private LocalDateTime timestamp;

}
