package ru.ural.contracts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCompletedContractsDto {

    private String userUuid;

    private Long completedContractsCount;

    private List<ContractDto> contracts;

}
