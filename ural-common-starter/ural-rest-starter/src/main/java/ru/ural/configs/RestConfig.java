package ru.ural.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.ural.handlers.HttpExceptionHandler;
import ru.ural.mappers.HttpExceptionMapper;

import java.util.List;

@Configuration
public class RestConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public HttpExceptionMapper httpExceptionMapper(List<HttpExceptionHandler> handlers) {
        return new HttpExceptionMapper(handlers);
    }

}
