package ru.ural.properties;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperty {

    @Nullable
    private String publicKey;

    @Nullable
    private String privateKey;

}

