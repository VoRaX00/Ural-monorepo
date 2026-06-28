package ural.ru.properties.proxy;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "proxy.files")
public class FileProperty implements ProxyProperty {

    @NotBlank
    private String url;

}
