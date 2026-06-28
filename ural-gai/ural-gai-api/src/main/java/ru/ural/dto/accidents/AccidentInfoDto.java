package ru.ural.dto.accidents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccidentInfoDto {

    private Boolean isAccident;

    private List<AccidentDto> accidents;

}
