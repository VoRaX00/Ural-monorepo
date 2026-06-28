package ru.ural.contracts.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "graphhopper")
public class GraphHopperProperties {

    private String baseUrl = "https://graphhopper.com/api/1";

    private String apiKey;

    private String profile = "car";

    private String locale = "ru";

}
