package ru.ural.contracts.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.ural.contracts.api.ContractsApi;
import ru.ural.contracts.dto.ContractDto;
import ru.ural.contracts.dto.ContractPriceOfferRequest;
import ru.ural.contracts.dto.ContractRatingDto;
import ru.ural.contracts.dto.ContractRatingRequest;
import ru.ural.contracts.dto.ContractRequest;
import ru.ural.contracts.dto.UserCompletedContractsDto;
import ru.ural.contracts.entities.Contract;
import ru.ural.contracts.mappers.ContractMapper;
import ru.ural.contracts.models.ContractModel;
import ru.ural.contracts.services.ContractService;
import ru.ural.dto.PageDto;
import ru.ural.dto.PaginatedParamsDto;

@RestController
@RequiredArgsConstructor
public class ContractController implements ContractsApi {

    private final ContractService contractService;

    private final ContractMapper contractMapper;

    @Override
    public ResponseEntity<ContractDto> getById(Long id) {
        var contract = contractService.getContractById(id);
        return ResponseEntity.ok(contractMapper.toDto(contract));
    }

    @Override
    public ResponseEntity<ContractDto> create(ContractRequest request) {
        Contract contract = contractMapper.toEntity(request);
        ContractModel model = contractService.create(contract);
        return ResponseEntity.ok(contractMapper.toDto(model));
    }

    @Override
    public ResponseEntity<ContractDto> edit(Long id, ContractRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<PageDto<ContractDto>> getPage(PaginatedParamsDto paginatedParamsDto) {
        return ResponseEntity.ok(contractService.getPageDto(paginatedParamsDto));
    }

    @Override
    public ResponseEntity<ContractDto> changeStatus(Long id, Boolean isClose) {
        return ResponseEntity.ok(contractService.changeStatus(id, isClose));
    }

    @Override
    public ResponseEntity<ContractDto> offerPrice(Long id, ContractPriceOfferRequest request) {
        return ResponseEntity.ok(contractService.offerPrice(id, request));
    }

    @Override
    public ResponseEntity<ContractRatingDto> rateContract(Long id, ContractRatingRequest request) {
        return ResponseEntity.ok(contractService.rateContract(id, request));
    }

    @Override
    public ResponseEntity<UserCompletedContractsDto> getUserCompletedContracts(String userUuid) {
        return ResponseEntity.ok(contractService.getUserCompletedContracts(userUuid));
    }
}
