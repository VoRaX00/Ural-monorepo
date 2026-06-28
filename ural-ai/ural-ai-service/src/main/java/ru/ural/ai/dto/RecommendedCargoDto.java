package ru.ural.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ural.cargo.dto.CargoDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedCargoDto {

    private CargoDto cargo;

    private Integer score;

    private List<String> reasons;

}
