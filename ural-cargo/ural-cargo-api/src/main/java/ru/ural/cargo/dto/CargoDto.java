package ru.ural.cargo.dto;

import java.math.*;
import java.time.*;
import java.util.List;

import lombok.*;
import ru.ural.dto.AddressDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CargoDto {

    private Long id;

    private String userUuid;

    private String name;

    private BigDecimal length;

    private BigDecimal width;

    private BigDecimal height;

    private BigDecimal weight;

    private AddressDto loadingPlace;

    private AddressDto unloadingPlace;

    private BigDecimal price;

    private String comment;

    private String status;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private List<Long> fileIds;

    private List<String> bodyTypes;

    private List<String> loadingTypes;

    private List<String> unloadingTypes;

}
