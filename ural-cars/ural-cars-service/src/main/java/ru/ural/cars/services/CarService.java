package ru.ural.cars.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.ural.ai.events.CarPhotoAnalysisRequestedEvent;
import ru.ural.cars.dto.CarDto;
import ru.ural.cars.dto.CarRequest;
import ru.ural.cars.entities.Car;
import ru.ural.cars.entities.GibddInfo;
import ru.ural.cars.enums.CarConditionStatus;
import ru.ural.cars.mappers.CarMapper;
import ru.ural.cars.mappers.PaginatedMapper;
import ru.ural.cars.producers.CarPhotoAnalysisProducer;
import ru.ural.cars.repositories.CarRepository;
import ru.ural.cars.repositories.CustomCarRepository;
import ru.ural.dto.PageDto;
import ru.ural.dto.PaginatedParamsDto;
import ru.ural.enums.UserRole;
import ru.ural.exceptions.BadRequestException;
import ru.ural.exceptions.ForbiddenException;
import ru.ural.exceptions.NotFoundException;
import ru.ural.models.UserPrincipals;
import ru.ural.utils.JwtUtils;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarService {

    private final CarMapper carMapper;

    private final PaginatedMapper paginatedMapper;

    private final CarRepository carRepository;

    private final CustomCarRepository customCarRepository;

    private final CarPhotoAnalysisProducer carPhotoAnalysisProducer;

    private final GibddInfoService gibddInfoService;

    @NonNull
    public CarDto create(@NonNull CarRequest request) {
        Car entity = carMapper.toEntity(request);

        UserPrincipals user = getUser();

        entity.setUserUuid(user.getUuid());
        entity.setCreatedAt(ZonedDateTime.now());
        entity.setBooked(false);
        markPhotoAnalysisPending(entity);

        Car savedCar = carRepository.save(entity);
        GibddInfo gibddInfo = gibddInfoService.refreshInfoOrThrow(savedCar);
        sendPhotoAnalysisRequest(savedCar, gibddInfo);
        return carMapper.toDto(savedCar);
    }

    @Transactional(readOnly = true)
    public CarDto findByVin(@NonNull String vin) {
        var foundCar = carRepository.findCarByVinNumber(vin)
                .orElseThrow(() -> new NotFoundException("Not found car by vin: %s".formatted(vin)));

        return carMapper.toDto(foundCar);
    }

    @Transactional(readOnly = true)
    public CarDto findById(@NonNull Long id) {
        var foundCar = carRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found car by id: %s".formatted(id)));

        return carMapper.toDto(foundCar);
    }

    @Transactional(readOnly = true)
    public PageDto<CarDto> getPage(PaginatedParamsDto paramsDto) {
        var paramsModel = paginatedMapper.toModel(paramsDto);
        Map<String, String> filters = paramsModel.getFilters();
        String requestedUserUuid = filters == null ? null : filters.get("userUuid");
        boolean showBooked = shouldShowBooked(requestedUserUuid);
        var items = customCarRepository.getItems(paramsModel, showBooked);
        int totalResultCount = customCarRepository.getTotalResultCount(filters, showBooked);
        int totalPageCount = totalResultCount % paramsModel.getItemsOnPage() == 0
                ? totalResultCount / paramsModel.getItemsOnPage()
                : totalResultCount / paramsModel.getItemsOnPage() + 1;

        return new PageDto<>(
                paramsModel.getCurrentPageNumber(),
                totalPageCount,
                totalResultCount,
                carMapper.toDto(items),
                paramsModel.getItemsOnPage()
        );
    }

    @Transactional
    public CarDto update(@NonNull Long id, @NonNull CarRequest request) {
        var savedCar = carRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found car by id: %s".formatted(id)));

        UserPrincipals user = getUser();
        if (!savedCar.getUserUuid().equals(user.getUuid())) {
            throw new ForbiddenException("Нельзя редактировать чужой транспорт");
        }

        carMapper.mapCarDtoToCar(request, savedCar);
        savedCar.setUpdatedAt(ZonedDateTime.now());

        var updatedCar = carRepository.save(savedCar);
        gibddInfoService.refreshInfo(updatedCar);
        return carMapper.toDto(updatedCar);
    }

    @Transactional
    public CarDto updateBooking(@NonNull Long id, @NonNull Boolean booked) {
        var savedCar = carRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found car by id: %s".formatted(id)));

        savedCar.setBooked(booked);
        savedCar.setUpdatedAt(ZonedDateTime.now());
        return carMapper.toDto(carRepository.save(savedCar));
    }

    @Transactional
    public CarDto retryPhotoAnalysis(@NonNull Long id) {
        var savedCar = carRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found car by id: %s".formatted(id)));

        UserPrincipals user = getUser();
        if (!savedCar.getUserUuid().equals(user.getUuid())) {
            throw new ForbiddenException("Нельзя запускать анализ чужого транспорта");
        }
        if (CollectionUtils.isEmpty(savedCar.getFileIds())) {
            throw new BadRequestException("Для анализа нужно добавить фотографии транспорта");
        }

        GibddInfo gibddInfo = gibddInfoService.refreshInfoOrThrow(savedCar);
        markPhotoAnalysisPending(savedCar);
        savedCar.setUpdatedAt(ZonedDateTime.now());
        var updatedCar = carRepository.save(savedCar);
        sendPhotoAnalysisRequest(updatedCar, gibddInfo);
        return carMapper.toDto(updatedCar);
    }

    @Transactional
    public void deleteById(Long id) {
        carRepository.deleteById(id);
    }

    private void markPhotoAnalysisPending(Car car) {
        if (CollectionUtils.isEmpty(car.getFileIds())) {
            return;
        }

        car.setPhotoAnalysisSummary("Анализ фотографий выполняется");
        car.setPhotoAnalysisStatus(CarConditionStatus.UNKNOWN);
    }

    private void sendPhotoAnalysisRequest(Car car, GibddInfo gibddInfo) {
        if (CollectionUtils.isEmpty(car.getFileIds())) {
            return;
        }

        try {
            carPhotoAnalysisProducer.send(CarPhotoAnalysisRequestedEvent.builder()
                    .carId(car.getId())
                    .fileIds(car.getFileIds())
                    .carInfo(buildCarInfo(car))
                    .autocodeInfo(buildAutocodeInfo(gibddInfo == null ? car.getGibddInfo() : gibddInfo))
                    .authorization(getAuthorization())
                    .build());
        } catch (RuntimeException exception) {
            log.error("Failed to send car photo analysis request for car id: {}", car.getId(), exception);
        }
    }

    private Map<String, Object> buildCarInfo(Car car) {
        Map<String, Object> carInfo = new LinkedHashMap<>();
        carInfo.put("id", car.getId());
        carInfo.put("carType", car.getCarType() == null ? null : car.getCarType().getValue());
        carInfo.put("carName", car.getCarName());
        carInfo.put("carModel", car.getCarModel());
        carInfo.put("bodyType", car.getBodyType() == null ? null : car.getBodyType().stream()
                .map(Enum::name)
                .toList());
        carInfo.put("loadingType", car.getLoadingType() == null ? null : car.getLoadingType().stream()
                .map(Enum::name)
                .toList());
        carInfo.put("loadCapacity", car.getLoadCapacity());
        carInfo.put("yearProduction", car.getYearProduction());
        carInfo.put("vinNumber", car.getVinNumber());
        return carInfo;
    }

    private Map<String, Object> buildAutocodeInfo(GibddInfo gibddInfo) {
        if (gibddInfo == null) {
            return Map.of();
        }

        Map<String, Object> autocodeInfo = new LinkedHashMap<>();
        autocodeInfo.put("ownersCount", gibddInfo.getOwnersCount());
        autocodeInfo.put("accidentsCount", gibddInfo.getAccidentsCount());
        autocodeInfo.put("hasRegistrationRestrictions", gibddInfo.getHasRegistrationRestrictions());
        autocodeInfo.put("isWanted", gibddInfo.getWanted());
        autocodeInfo.put("isPledged", gibddInfo.getPledged());
        autocodeInfo.put("lastCheckAt", gibddInfo.getLastCheckAt());
        autocodeInfo.put("rawResponse", gibddInfo.getRawResponse());
        return autocodeInfo;
    }

    private UserPrincipals getUser() {
        Authentication authentication = JwtUtils.getAuthentication();
        return JwtUtils.getUser(authentication);
    }

    private boolean shouldShowBooked(String requestedUserUuid) {
        UserPrincipals user = getUser();
        return user != null
                && (user.getRoles().contains(UserRole.ADMIN)
                || Objects.equals(requestedUserUuid, user.getUuid()));
    }

    private String getAuthorization() {
        var requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return null;
        }

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        return servletRequestAttributes.getRequest().getHeader("Authorization");
    }

}
