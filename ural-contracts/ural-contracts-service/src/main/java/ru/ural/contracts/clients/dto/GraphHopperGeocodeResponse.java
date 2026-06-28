package ru.ural.contracts.clients.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraphHopperGeocodeResponse {

    private List<GraphHopperGeocodeHit> hits;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GraphHopperGeocodeHit {

        private String name;

        private String country;

        private String city;

        private GraphHopperPoint point;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GraphHopperPoint {

        private BigDecimal lat;

        private BigDecimal lng;

    }

}
