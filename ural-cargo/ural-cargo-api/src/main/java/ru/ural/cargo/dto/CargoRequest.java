package ru.ural.cargo.dto;

import java.math.*;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import ru.ural.dto.AddressDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CargoRequest {

    @NotNull
    @Schema(description = "Наименование груза")
    @Size(max = 255, min = 2, message = "Название груза должно состоять из 2 до 255 символов")
    private String name;

    @NotNull
    @Schema(description = "Длина груза")
    private BigDecimal length;

    @NotNull
    @Schema(description = "Ширина груза")
    private BigDecimal width;

    @NotNull
    @Schema(description = "Высота груза")
    private BigDecimal height;

    @NotNull
    @Schema(description = "Вес груза")
    private BigDecimal weight;

    @NotNull
    @Schema(description = "Место загрузки")
    private AddressDto loadingPlace;

    @NotNull
    @Schema(description = "Место разгрузки")
    private AddressDto unloadingPlace;

    @NotNull
    @Schema(description = "Желаемая стоимость")
    private BigDecimal price;

    @Schema(description = "Комментарий")
    private String comment;

    @NotEmpty
    @Schema(description = "Id файлов груза")
    private List<Long> fileIds;

    @NotEmpty
    @Schema(description = "Подходящие типы кузова")
    private List<@NotBlank String> bodyTypes;

    @NotEmpty
    @Schema(description = "Подходящие типы загрузки")
    private List<@NotBlank String> loadingTypes;

    @NotEmpty
    @Schema(description = "Подходящие типы разгрузки")
    private List<@NotBlank String> unloadingTypes;

}
