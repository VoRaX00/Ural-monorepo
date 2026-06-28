package ru.ural.contracts.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckpointAddress {

    private String country;

    private String city;

    private String street;

    private String house;

    private String apartment;

    private String postalCode;

}
