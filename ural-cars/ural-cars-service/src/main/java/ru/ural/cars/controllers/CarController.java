package ru.ural.cars.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.ural.cars.api.CarApi;
import ru.ural.cars.dto.CarDictionariesDto;
import ru.ural.cars.dto.CarDto;
import ru.ural.cars.dto.CarRequest;
import ru.ural.cars.services.CarDictionaryService;
import ru.ural.cars.services.CarService;
import ru.ural.dto.PageDto;
import ru.ural.dto.PaginatedParamsDto;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CarController implements CarApi {

    private final CarService carService;

    private final CarDictionaryService carDictionaryService;

    @Override
    public ResponseEntity<CarDto> create(CarRequest carRequest) {
        return new ResponseEntity<>(carService.create(carRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CarDto> getByVin(String vin) {
        return ResponseEntity.ok(carService.findByVin(vin));
    }

    @Override
    public ResponseEntity<CarDto> getById(Long id) {
        return ResponseEntity.ok(carService.findById(id));
    }

    @Override
    public ResponseEntity<PageDto<CarDto>> getByFilters(PaginatedParamsDto paramsDto) {
        return ResponseEntity.ok(carService.getPage(paramsDto));
    }

    @Override
    public ResponseEntity<CarDictionariesDto> getDictionaries() {
        return ResponseEntity.ok(carDictionaryService.getDictionaries());
    }

    @Override
    public ResponseEntity<CarDto> update(Long id, CarRequest carRequest) {
        return ResponseEntity.ok(carService.update(id, carRequest));
    }

    @Override
    public ResponseEntity<CarDto> retryPhotoAnalysis(Long id) {
        return ResponseEntity.ok(carService.retryPhotoAnalysis(id));
    }

    @Override
    public ResponseEntity<CarDto> updateBooking(Long id, Boolean booked) {
        return ResponseEntity.ok(carService.updateBooking(id, booked));
    }

    @Override
    public ResponseEntity<Void> deleteById(Long id) {
        carService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
