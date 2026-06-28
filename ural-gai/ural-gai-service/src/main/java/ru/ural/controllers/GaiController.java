package ru.ural.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.ural.api.GaiApi;
import ru.ural.dto.InspectionDto;
import ru.ural.dto.accidents.AccidentInfoDto;
import ru.ural.dto.autocode.AutocodeInfoDto;
import ru.ural.dto.wanted.WantedInfoDto;
import ru.ural.services.GaiService;

@RestController
@RequiredArgsConstructor
public class GaiController implements GaiApi {

    private final GaiService gaiService;

    @Override
    public ResponseEntity<WantedInfoDto> wanted(String vin) {
        return ResponseEntity.ok(gaiService.getWantedInfo(vin));
    }

    @Override
    public ResponseEntity<InspectionDto> inspection(String vin) {
        return ResponseEntity.ok(gaiService.getInspectionInfo(vin));
    }

    @Override
    public ResponseEntity<AccidentInfoDto> accident(String vin) {
        return ResponseEntity.ok(gaiService.getAccidentInfo(vin));
    }

    @Override
    public ResponseEntity<AutocodeInfoDto> autocode(String vin) {
        return ResponseEntity.ok(gaiService.getAutocodeInfo(vin));
    }

}
