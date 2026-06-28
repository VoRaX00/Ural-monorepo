package ru.ural.cargo.services;

import java.time.*;
import java.util.Map;
import java.util.Objects;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;
import ru.ural.cargo.dto.CargoDto;
import ru.ural.cargo.dto.CargoRequest;
import ru.ural.cargo.entities.Cargo;
import ru.ural.cargo.enums.CargoStatus;
import ru.ural.dto.PageDto;
import ru.ural.dto.PaginatedParamsDto;
import ru.ural.enums.UserRole;
import ru.ural.exceptions.BadRequestException;
import ru.ural.exceptions.NotFoundException;
import ru.ural.cargo.mappers.CargoMapper;
import ru.ural.cargo.mappers.PaginatedMapper;
import ru.ural.models.UserPrincipals;
import ru.ural.cargo.repositories.CargoRepository;
import ru.ural.cargo.repositories.CustomCargoRepository;
import ru.ural.utils.JwtUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class CargoService {

    private final CargoRepository cargoRepository;

    private final CustomCargoRepository customCargoRepository;

    private final PaginatedMapper paginatedMapper;

    private final CargoMapper cargoMapper;

    public CargoDto create(@NonNull CargoRequest cargoRequest) {
        var cargo = cargoMapper.toEntity(cargoRequest);
        cargo.setStatus(CargoStatus.SEARCH);
        cargo.setCreatedAt(ZonedDateTime.now());

        Authentication authentication = JwtUtils.getAuthentication();
        UserPrincipals user = JwtUtils.getUser(authentication);
        cargo.setUserUuid(user.getUuid());

        var savedCargo = cargoRepository.save(cargo);
        return cargoMapper.toDto(savedCargo);
    }

    @Transactional(readOnly = true)
    public PageDto<CargoDto> getPage(PaginatedParamsDto paramsDto) {
        var paramsModel = paginatedMapper.toModel(paramsDto);
        Map<String, String> filters = paramsModel.getFilters();
        String requestedUserUuid = filters == null ? null : filters.get("userUuid");
        boolean showUnavailable = shouldShowUnavailable(requestedUserUuid);
        var items = customCargoRepository.getItems(paramsModel, showUnavailable);
        int totalResultCount = customCargoRepository.getTotalResultCount(filters, showUnavailable);
        int totalPageCount = totalResultCount % paramsModel.getItemsOnPage() == 0
                ? totalResultCount / paramsModel.getItemsOnPage()
                : totalResultCount / paramsModel.getItemsOnPage() + 1;

        return new PageDto<>(
                paramsModel.getCurrentPageNumber(),
                totalPageCount,
                totalResultCount,
                cargoMapper.toDto(items),
                paramsModel.getItemsOnPage()
        );
    }

    @Transactional(readOnly = true)
    public CargoDto getById(Long id) {
        Cargo cargo = cargoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "Груз с id: %d не найден", id
                )));

        return cargoMapper.toDto(cargo);
    }

    @Transactional
    public CargoDto updateStatus(Long id, String status) {
        Cargo cargo = cargoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "Груз с id: %d не найден", id
                )));

        cargo.setStatus(CargoStatus.parse(status));
        cargo.setUpdatedAt(ZonedDateTime.now());
        return cargoMapper.toDto(cargoRepository.save(cargo));
    }

    @Transactional
    public CargoDto update(
            @NonNull Long id,
            @NonNull CargoRequest cargoRequest
    ) {
        var cargoFromDb = cargoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "Cargo not found by id: %d",
                        id
                )));

        Authentication authentication = JwtUtils.getAuthentication();
        UserPrincipals user = JwtUtils.getUser(authentication);
        if (!Objects.equals(cargoFromDb.getUserUuid(), user.getUuid())) {
            throw new BadRequestException("Only maintainer can update cargo");
        }

        cargoMapper.mapCargoDtoToEntity(cargoFromDb, cargoRequest);
        if (cargoFromDb.getStatus() == null) {
            cargoFromDb.setStatus(CargoStatus.SEARCH);
        }
        cargoFromDb.setUpdatedAt(ZonedDateTime.now());
        var updatedCargo = cargoRepository.save(cargoFromDb);
        return cargoMapper.toDto(updatedCargo);
    }

    @Transactional
    public void delete(Long id) {
        var cargoFromDb = cargoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "Cargo not found by id: %d",
                        id
                )));

        Authentication authentication = JwtUtils.getAuthentication();
        UserPrincipals user = JwtUtils.getUser(authentication);
        if (!Objects.equals(cargoFromDb.getUserUuid(), user.getUuid())
                && !user.getRoles().contains(UserRole.ADMIN)) {
            throw new BadRequestException("Only maintainer can delete cargo");
        }

        cargoRepository.deleteById(id);
    }

    private boolean shouldShowUnavailable(String requestedUserUuid) {
        UserPrincipals user = getUser();
        return user != null
                && (user.getRoles().contains(UserRole.ADMIN)
                || Objects.equals(requestedUserUuid, user.getUuid()));
    }

    private UserPrincipals getUser() {
        Authentication authentication = JwtUtils.getAuthentication();
        return JwtUtils.getUser(authentication);
    }

}
