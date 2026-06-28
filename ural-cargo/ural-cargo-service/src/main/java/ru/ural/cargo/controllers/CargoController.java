package ru.ural.cargo.controllers;

import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.ural.cargo.api.CargoApi;
import ru.ural.cargo.dto.CargoDictionariesDto;
import ru.ural.cargo.dto.CargoDto;
import ru.ural.cargo.dto.CargoRequest;
import ru.ural.cargo.services.CargoDictionaryService;
import ru.ural.cargo.services.CargoService;
import ru.ural.dto.PageDto;
import ru.ural.dto.PaginatedParamsDto;

@RestController
@RequiredArgsConstructor
public class CargoController implements CargoApi {

    private final CargoService cargoService;

    private final CargoDictionaryService cargoDictionaryService;

    @Override
    public ResponseEntity<CargoDto> create(CargoRequest cargoRequest) {
        return new ResponseEntity<>(cargoService.create(cargoRequest), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CargoDto> update(Long id, CargoRequest cargoRequest) {
        return ResponseEntity.ok(cargoService.update(id, cargoRequest));
    }

    @Override
    public ResponseEntity<PageDto<CargoDto>> getPaginatedList(PaginatedParamsDto paramsDto) {
        return ResponseEntity.ok(cargoService.getPage(paramsDto));
    }

    @Override
    public ResponseEntity<CargoDictionariesDto> getDictionaries() {
        return ResponseEntity.ok(cargoDictionaryService.getDictionaries());
    }

    @Override
    public ResponseEntity<CargoDto> getById(Long id) {
        return ResponseEntity.ok(cargoService.getById(id));
    }

    @Override
    public ResponseEntity<CargoDto> updateStatus(Long id, String status) {
        return ResponseEntity.ok(cargoService.updateStatus(id, status));
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        cargoService.delete(id);
        return ResponseEntity.ok().build();
    }

}
