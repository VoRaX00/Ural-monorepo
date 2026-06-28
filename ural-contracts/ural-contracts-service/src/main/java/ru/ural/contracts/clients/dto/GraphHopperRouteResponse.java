package ru.ural.contracts.clients.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraphHopperRouteResponse {

    private List<GraphHopperPath> paths;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GraphHopperPath {

        private BigDecimal distance;

        private Long time;

        private GraphHopperPoints points;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GraphHopperPoints {

        private List<List<BigDecimal>> coordinates;

    }

}
