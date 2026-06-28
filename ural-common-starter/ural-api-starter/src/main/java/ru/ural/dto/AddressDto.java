package ru.ural.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    @NotNull
    @Schema(description = "Страна")
    private String country;

    @NotNull
    @Schema(description = "Город")
    private String city;

    @NotNull
    @Schema(description = "Улица")
    private String street;

    @NotNull
    @Schema(description = "Дом")
    private String house;

    @Schema(description = "Квартира")
    private String apartment;

    @Schema(description = "Почтовый индекс")
    private String postalCode;

}
