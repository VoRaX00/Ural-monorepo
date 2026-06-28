package ru.ural.cars.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String country;

    private String city;

    private String street;

    private String house;

    private String apartment;

    private String postalCode;

}
