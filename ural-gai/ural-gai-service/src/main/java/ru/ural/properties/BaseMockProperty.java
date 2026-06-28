package ru.ural.properties;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Getter
@Setter
@Validated
public class BaseMockProperty {

    @NotEmpty
    private List<String> goodVins;

}
