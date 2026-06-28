package ru.ural.dto.wanted;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WantedDto {

    private LocalDate startDate;

    private LocalDate endDate;

    private String description;

}
