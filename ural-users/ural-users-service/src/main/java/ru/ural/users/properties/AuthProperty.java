package ru.ural.users.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "auth")
public class AuthProperty {

    private String url;

    private String pathRegistration;

}
