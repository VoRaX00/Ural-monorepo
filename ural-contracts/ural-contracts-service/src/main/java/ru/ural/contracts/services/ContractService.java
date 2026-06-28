package ru.ural.contracts.services;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ural.cargo.dto.CargoDto;
import ru.ural.cars.dto.CarDto;
import ru.ural.contracts.dto.ContractDto;
import ru.ural.contracts.dto.ContractPriceOfferRequest;
import ru.ural.contracts.dto.ContractRatingDto;
import ru.ural.contracts.dto.ContractRatingRequest;
import ru.ural.contracts.dto.UserCompletedContractsDto;
import ru.ural.contracts.entities.Contract;
import ru.ural.contracts.entities.ContractRating;
import ru.ural.contracts.enums.ContractStatus;
import ru.ural.contracts.events.UserRatingUpdatedEvent;
import ru.ural.contracts.mappers.PaginatedMapper;
import ru.ural.contracts.repositories.CustomContractRepository;
import ru.ural.contracts.repositories.ContractRatingRepository;
import ru.ural.contracts.senders.CargoSender;
import ru.ural.contracts.senders.CarsSender;
import ru.ural.contracts.senders.NotificationSender;
import ru.ural.contracts.senders.UserRatingSender;
import ru.ural.dto.PageDto;
import ru.ural.dto.PaginatedParamsDto;
import ru.ural.exceptions.BadRequestException;
import ru.ural.exceptions.ForbiddenException;
import ru.ural.exceptions.NotFoundException;
import ru.ural.contracts.mappers.ContractMapper;
import ru.ural.contracts.models.ContractModel;
import ru.ural.models.UserPrincipals;
import ru.ural.contracts.repositories.ContractRepository;
import ru.ural.notifications.dto.contract.NotificationContractRequest;
import ru.ural.utils.JwtUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ContractService {

    private static final String NOTIFICATION_TITLE_AFTER_UPDATE_STATUS = "Изменен статус контракта";

    private static final String NOTIFICATION_BODY_AFTER_UPDATE_STATUS = "Статус контракта изменен на %s";

    private static final String NOTIFICATION_TITLE_FOR_CREATE = "Появился новый контракт";

    private static final String NOTIFICATION_BODY_FOR_CREATE = "Добрый день! У вам предложили новый контракт";

    private static final String NOTIFICATION_TITLE_FOR_PRICE_OFFER = "Предложена новая стоимость контракта";

    private static final String NOTIFICATION_BODY_FOR_PRICE_OFFER = "По контракту предложена новая стоимость: %s";

    private static final String CARGO_STATUS_SEARCH = "SEARCH";

    private static final String CARGO_STATUS_AWAITING = "AWAITING";

    private static final String CARGO_STATUS_PROCESS = "PROCESS";

    private static final String CARGO_STATUS_DELIVERED = "DELIVERED";

    private final CarsSender carsSender;

    private final CargoSender cargoSender;

    private final ContractRepository contractRepository;

    private final ContractRatingRepository contractRatingRepository;

    private final CustomContractRepository customContractRepository;

    private final ContractMapper contractMapper;

    private final PaginatedMapper paginatedMapper;

    private final NotificationSender notificationSender;

    private final UserRatingSender userRatingSender;

    private final RouteService routeService;

    @NonNull
    @Transactional(readOnly = true)
    public ContractModel getContractById(@NonNull Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "Not found contract by id: %d", id
                )));

        ContractModel model = contractMapper.toModel(contract);
        CarDto carDto = carsSender.getCarById(contract.getCarId());
        CargoDto cargoDto = cargoSender.getCargoById(contract.getCargoId());

        model.setCar(carDto);
        model.setCargo(cargoDto);
        model.setRoute(contractMapper.toDto(contract.getRoute()));
        return model;
    }

    public ContractModel create(@NonNull Contract contract) {
        String ownerUuid = getUserUuid();

        contract.setCreatedAt(ZonedDateTime.now());
        contract.setStatus(ContractStatus.AGREEMENT);
        contract.setOwnerUuid(ownerUuid);
        contract.setPriceRequestedByUuid(ownerUuid);
        contract.setPriceUpdatedAt(contract.getCreatedAt());

        CarDto carDto = carsSender.getCarById(contract.getCarId());
        CargoDto cargoDto = cargoSender.getCargoById(contract.getCargoId());

        String relatedUserUuid = ownerUuid.equals(carDto.getUserUuid())
                ? cargoDto.getUserUuid()
                : carDto.getUserUuid();

        contract.setRelatedUserUuid(relatedUserUuid);

        Contract savedDb = contractRepository.save(contract);

        ContractModel model = contractMapper.toModel(savedDb);
        model.setCar(carDto);
        model.setCargo(cargoDto);

        NotificationContractRequest notification = buildCreateNotification(savedDb.getId(), relatedUserUuid);
        notificationSender.sendContractNotification(notification);
        return model;
    }

    public PageDto<ContractDto> getPageDto(PaginatedParamsDto paginatedParamsDto) {
        var paramsModel = paginatedMapper.toModel(paginatedParamsDto);
        var items = customContractRepository.getItems(paramsModel);
        int totalResultCount = customContractRepository.getTotalResultCount(paramsModel.getFilters());
        int totalPageCount = totalResultCount % paramsModel.getItemsOnPage() == 0
                ? totalResultCount / paramsModel.getItemsOnPage()
                : totalResultCount / paramsModel.getItemsOnPage() + 1;

        var pageDto = new PageDto<>(
                paramsModel.getCurrentPageNumber(), totalPageCount,
                totalResultCount,
                contractMapper.toDto(items),
                paramsModel.getItemsOnPage()
        );

        pageDto.getItems().forEach(item -> {
            var cargoId = item.getCargo().getId();
            var carId = item.getCar().getId();

            item.setCargo(cargoSender.getCargoById(cargoId));
            item.setCar(carsSender.getCarById(carId));
        });

        return pageDto;
    }

    @Transactional
    public ContractDto changeStatus(Long id, Boolean isClose) {
        String currentUserUuid = getUserUuid();
        Contract contract = updateContractStatus(id, isClose, currentUserUuid);

        String notificationReceiverUuid = getNotificationReceiverUuid(contract, currentUserUuid);

        var cargo = cargoSender.getCargoById(contract.getCargoId());
        var car = carsSender.getCarById(contract.getCarId());
        if (contract.getStatus() == ContractStatus.READY_EXECUTION) {
            routeService.buildRoute(contract, cargo);
        }
        updateRelatedResourceAvailability(contract);

        notificationSender.sendContractNotification(buildStatusChangedNotification(
                contract.getId(), contract.getStatus(), notificationReceiverUuid
        ));

        return buildContractDto(contract, cargo, car);
    }

    @Transactional
    public ContractDto offerPrice(Long id, ContractPriceOfferRequest request) {
        Contract contract = getContractEntityById(id);
        String currentUserUuid = getUserUuid();

        validateAgreementPriceResponder(contract, currentUserUuid);

        ZonedDateTime now = ZonedDateTime.now();
        contract.setPrice(request.getPrice());
        contract.setPriceRequestedByUuid(currentUserUuid);
        contract.setPriceUpdatedAt(now);
        contract.setUpdatedAt(now);

        String notificationReceiverUuid = getNotificationReceiverUuid(contract, currentUserUuid);
        notificationSender.sendContractNotification(buildPriceOfferNotification(
                contract.getId(), contract.getPrice(), notificationReceiverUuid
        ));

        var cargo = cargoSender.getCargoById(contract.getCargoId());
        var car = carsSender.getCarById(contract.getCarId());
        return buildContractDto(contract, cargo, car);
    }

    public ContractRatingDto rateContract(Long id, ContractRatingRequest request) {
        Contract contract = getContractEntityById(id);
        if (contract.getStatus() != ContractStatus.FINISHED) {
            throw new BadRequestException("Оценить можно только завершенный контракт");
        }

        String currentUserUuid = getUserUuid();
        if (!isParticipant(contract, currentUserUuid)) {
            throw new ForbiddenException("Оценить контракт может только участник сделки");
        }

        String ratedUserUuid = getNotificationReceiverUuid(contract, currentUserUuid);
        ZonedDateTime now = ZonedDateTime.now();
        ContractRating rating = contractRatingRepository
                .findByContractIdAndRaterUserUuid(contract.getId(), currentUserUuid)
                .orElseGet(() -> ContractRating.builder()
                        .contract(contract)
                        .raterUserUuid(currentUserUuid)
                        .ratedUserUuid(ratedUserUuid)
                        .createdAt(now)
                        .build());

        rating.setRatedUserUuid(ratedUserUuid);
        rating.setRating(request.getRating());
        rating.setUpdatedAt(now);

        ContractRating savedRating = contractRatingRepository.save(rating);
        userRatingSender.sendUserRatingUpdated(buildUserRatingUpdatedEvent(ratedUserUuid));

        return toRatingDto(savedRating);
    }

    public UserCompletedContractsDto getUserCompletedContracts(String userUuid) {
        List<Contract> contracts = contractRepository
                .findCompletedByUser(ContractStatus.FINISHED, userUuid);
        List<ContractDto> contractDtos = contractMapper.toDto(contracts);
        contractDtos.forEach(item -> {
            Long cargoId = item.getCargo().getId();
            Long carId = item.getCar().getId();

            item.setCargo(cargoSender.getCargoById(cargoId));
            item.setCar(carsSender.getCarById(carId));
        });

        return UserCompletedContractsDto.builder()
                .userUuid(userUuid)
                .completedContractsCount((long) contracts.size())
                .contracts(contractDtos)
                .build();
    }

    private Contract updateContractStatus(Long id, Boolean isClose, String currentUserUuid) {
        Contract contract = getContractEntityById(id);
        if (!isParticipant(contract, currentUserUuid)) {
            throw new ForbiddenException("Изменить статус контракта может только участник сделки");
        }

        if (contract.getStatus() == ContractStatus.AGREEMENT) {
            validateAgreementPriceResponder(contract, currentUserUuid);
        }

        ContractStatus newStatus = resolveNewStatus(contract.getStatus(), isClose);
        contract.setStatus(newStatus);
        contract.setUpdatedAt(ZonedDateTime.now());
        return contract;
    }

    private Contract getContractEntityById(Long id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Контракт с id %d не найден".formatted(id)));
    }

    private ContractStatus resolveNewStatus(ContractStatus currentStatus, Boolean isClose) {
        return Boolean.TRUE.equals(isClose)
                ? ContractStatus.CLOSED
                : getNextStatus(currentStatus);
    }

    private void updateRelatedResourceAvailability(Contract contract) {
        switch (contract.getStatus()) {
            case READY_EXECUTION -> {
                cargoSender.updateCargoStatus(contract.getCargoId(), CARGO_STATUS_AWAITING);
                carsSender.updateCarBooking(contract.getCarId(), true);
            }
            case PROCESS -> {
                cargoSender.updateCargoStatus(contract.getCargoId(), CARGO_STATUS_PROCESS);
                carsSender.updateCarBooking(contract.getCarId(), true);
            }
            case FINISHED -> {
                cargoSender.updateCargoStatus(contract.getCargoId(), CARGO_STATUS_DELIVERED);
                carsSender.updateCarBooking(contract.getCarId(), false);
            }
            case CLOSED -> {
                cargoSender.updateCargoStatus(contract.getCargoId(), CARGO_STATUS_SEARCH);
                carsSender.updateCarBooking(contract.getCarId(), false);
            }
            default -> {
            }
        }
    }

    private NotificationContractRequest buildStatusChangedNotification(
            Long contractId,
            ContractStatus newStatus,
            String receiverUuid
    ) {
        return NotificationContractRequest.builder()
                .title(NOTIFICATION_TITLE_AFTER_UPDATE_STATUS)
                .body(NOTIFICATION_BODY_AFTER_UPDATE_STATUS.formatted(newStatus.getValue()))
                .contractId(contractId)
                .userUuids(List.of(receiverUuid))
                .build();
    }

    private NotificationContractRequest buildCreateNotification(
            Long contractId,
            String userUuid
    ) {
        return NotificationContractRequest.builder()
                .title(NOTIFICATION_TITLE_FOR_CREATE)
                .body(NOTIFICATION_BODY_FOR_CREATE)
                .contractId(contractId)
                .userUuids(List.of(userUuid))
                .build();
    }

    private NotificationContractRequest buildPriceOfferNotification(
            Long contractId,
            BigDecimal price,
            String userUuid
    ) {
        return NotificationContractRequest.builder()
                .title(NOTIFICATION_TITLE_FOR_PRICE_OFFER)
                .body(NOTIFICATION_BODY_FOR_PRICE_OFFER.formatted(price))
                .contractId(contractId)
                .userUuids(List.of(userUuid))
                .build();
    }

    private void validateAgreementPriceResponder(Contract contract, String currentUserUuid) {
        if (contract.getStatus() != ContractStatus.AGREEMENT) {
            throw new BadRequestException("Стоимость можно согласовывать только на этапе согласования");
        }

        if (!isParticipant(contract, currentUserUuid)) {
            throw new ForbiddenException("Согласовывать стоимость может только участник сделки");
        }

        if (Objects.equals(contract.getPriceRequestedByUuid(), currentUserUuid)) {
            throw new ForbiddenException("Нельзя ответить на собственное предложение стоимости");
        }
    }

    private String getNotificationReceiverUuid(Contract contract, String currentUserUuid) {
        return Objects.equals(contract.getOwnerUuid(), currentUserUuid)
                ? contract.getRelatedUserUuid()
                : contract.getOwnerUuid();
    }

    private boolean isParticipant(Contract contract, String userUuid) {
        return Objects.equals(contract.getOwnerUuid(), userUuid)
                || Objects.equals(contract.getRelatedUserUuid(), userUuid);
    }

    private ContractDto buildContractDto(Contract contract, CargoDto cargo, CarDto car) {
        ContractDto dto = contractMapper.toDto(contract);
        dto.setCargo(cargo);
        dto.setCar(car);

        return dto;
    }

    private String getUserUuid() {
        Authentication authentication = JwtUtils.getAuthentication();
        UserPrincipals userPrincipals = JwtUtils.getUser(authentication);
        return userPrincipals.getUuid();
    }

    private BigDecimal getAverageRating(String userUuid) {
        if (contractRatingRepository.countByRatedUserUuid(userUuid) == 0) {
            return BigDecimal.valueOf(5).setScale(2, RoundingMode.HALF_UP);
        }

        Double average = contractRatingRepository.getAverageRating(userUuid);
        if (average == null) {
            return BigDecimal.valueOf(5).setScale(2, RoundingMode.HALF_UP);
        }

        return BigDecimal.valueOf(average).setScale(2, RoundingMode.HALF_UP);
    }

    private UserRatingUpdatedEvent buildUserRatingUpdatedEvent(String userUuid) {
        return UserRatingUpdatedEvent.builder()
                .userUuid(userUuid)
                .averageRating(getAverageRating(userUuid))
                .ratingsCount(contractRatingRepository.countByRatedUserUuid(userUuid))
                .build();
    }

    private ContractRatingDto toRatingDto(ContractRating rating) {
        return ContractRatingDto.builder()
                .id(rating.getId())
                .contractId(rating.getContract().getId())
                .raterUserUuid(rating.getRaterUserUuid())
                .ratedUserUuid(rating.getRatedUserUuid())
                .rating(rating.getRating())
                .createdAt(rating.getCreatedAt())
                .updatedAt(rating.getUpdatedAt())
                .build();
    }

    private ContractStatus getNextStatus(ContractStatus current) {
        if (current == ContractStatus.FINISHED || current == ContractStatus.CLOSED) {
            return current;
        }

        int index = current.ordinal() + 1;
        return ContractStatus.values()[index];
    }

}
