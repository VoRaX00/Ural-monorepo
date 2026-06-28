package ru.ural.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.ural.dto.InspectionDto;
import ru.ural.dto.accidents.AccidentInfoDto;
import ru.ural.dto.autocode.AutocodeInfoDto;
import ru.ural.dto.wanted.WantedInfoDto;

@RequestMapping("/api/gai")
@Tag(name = "API интеграции с ГАИ")
public interface    GaiApi {

    @Operation(summary = "Информация о штрафах")
    @GetMapping("/wanted/{vin}")
    ResponseEntity<WantedInfoDto> wanted(@PathVariable String vin);

    @Operation(summary = "Информация о обслуживании")
    @GetMapping("/inspection/{vin}")
    ResponseEntity<InspectionDto> inspection(@PathVariable String vin);

    @Operation(summary = "Информация о происшествиях")
    @GetMapping("/accident/{vin}")
    ResponseEntity<AccidentInfoDto> accident(@PathVariable String vin);

    @Operation(summary = "Полный мок отчета Автокод по VIN")
    @GetMapping("/autocode/{vin}")
    ResponseEntity<AutocodeInfoDto> autocode(@PathVariable String vin);

}
