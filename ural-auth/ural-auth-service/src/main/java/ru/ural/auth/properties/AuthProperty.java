package ru.ural.auth.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "security")
public class AuthProperty {

    @NotNull
    private Integer refreshTokenExpiredOnSeconds;

    @NotNull
    private Integer accessTokenExpiredOnSeconds;

}
