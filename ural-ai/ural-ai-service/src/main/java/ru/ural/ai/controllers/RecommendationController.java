package ru.ural.ai.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ural.ai.dto.RecommendedCarDto;
import ru.ural.ai.dto.RecommendedCargoDto;
import ru.ural.ai.services.RecommendationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/cargo/{cargoId}/cars")
    public ResponseEntity<List<RecommendedCarDto>> getRecommendedCars(
            @PathVariable Long cargoId,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization
    ) {
        return ResponseEntity.ok(recommendationService.getRecommendedCars(cargoId, authorization));
    }

    @GetMapping("/cars/{carId}/cargo")
    public ResponseEntity<List<RecommendedCargoDto>> getRecommendedCargo(
            @PathVariable Long carId,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization
    ) {
        return ResponseEntity.ok(recommendationService.getRecommendedCargo(carId, authorization));
    }

}
