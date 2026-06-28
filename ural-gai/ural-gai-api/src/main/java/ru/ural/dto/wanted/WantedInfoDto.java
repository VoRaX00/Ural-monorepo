package ru.ural.dto.wanted;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WantedInfoDto {

    private Boolean isWanted;

    private List<WantedDto> wantedList;

}
