package ru.ural.contracts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractRatingDto {

    private Long id;

    private Long contractId;

    private String raterUserUuid;

    private String ratedUserUuid;

    private Integer rating;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

}
