package ru.ural.cargo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.*;
import jakarta.validation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ural.cargo.dto.CargoDictionariesDto;
import ru.ural.cargo.dto.CargoDto;
import ru.ural.cargo.dto.CargoRequest;
import ru.ural.dto.PageDto;
import ru.ural.dto.PaginatedParamsDto;

@Validated
@RequestMapping("/api/cargo")
@Tag(name = "Cargo api", description = "API для с грузами")
public interface CargoApi {

    @Operation(summary = "Создать груз")
    @PostMapping
    ResponseEntity<CargoDto> create(@RequestBody @Valid CargoRequest cargoRequest);

    @Operation(summary = "Изменить груз")
    @PutMapping("/{id}")
    ResponseEntity<CargoDto> update(@PathVariable Long id, @RequestBody @Valid CargoRequest cargoRequest);

    @Operation(summary = "Получить пагинированный список грузов")
    @GetMapping
    ResponseEntity<PageDto<CargoDto>> getPaginatedList(PaginatedParamsDto paginatedParamsDto);

    @Operation(summary = "Получить справочники грузов")
    @GetMapping("/dictionaries")
    ResponseEntity<CargoDictionariesDto> getDictionaries();

    @Operation(summary = "Получить груз по id")
    @GetMapping("/{id}")
    ResponseEntity<CargoDto> getById(@PathVariable Long id);

    @Operation(summary = "Изменить статус груза")
    @RequestMapping(path = "/{id}/status", method = {RequestMethod.PATCH, RequestMethod.PUT})
    ResponseEntity<CargoDto> updateStatus(@PathVariable Long id, @RequestParam String status);

    @Operation(summary = "Удалить груз")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);

}
