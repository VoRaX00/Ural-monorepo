package ru.ural.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ural.cars.dto.CarDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedCarDto {

    private CarDto car;

    private Integer score;

    private List<String> reasons;

}
