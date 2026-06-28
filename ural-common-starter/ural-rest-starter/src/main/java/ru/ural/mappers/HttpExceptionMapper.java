package ru.ural.mappers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import ru.ural.exceptions.InternalServerException;
import ru.ural.exceptions.RestBaseException;
import ru.ural.handlers.HttpExceptionHandler;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class HttpExceptionMapper {

    private final List<HttpExceptionHandler> handlers;

    public RestBaseException map(HttpStatusCodeException exception) {
        HttpStatus status = HttpStatus.valueOf(exception.getStatusCode().value());

        return handlers.stream()
                .filter(handler -> handler.supports(status))
                .findFirst()
                .map(handler -> handler.handle(exception))
                .orElseGet(() -> defaultHandler(exception));
    }

    private RestBaseException defaultHandler(HttpStatusCodeException exception) {
        log.warn("Used default handler for exception: {}", exception.getStatusCode().value());
        return new InternalServerException(exception.getResponseBodyAsString(), exception);
    }

}
