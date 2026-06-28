package ru.ural.contracts.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "contract-integrations")
public class ContractIntegrationProperty {

    private String carsUrl;

    private String cargoUrl;

}
