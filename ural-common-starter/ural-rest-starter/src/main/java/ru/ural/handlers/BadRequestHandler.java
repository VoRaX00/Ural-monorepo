package ru.ural.handlers;

import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import ru.ural.exceptions.BadRequestException;
import ru.ural.exceptions.RestBaseException;

@Component
public class BadRequestHandler implements HttpExceptionHandler {
    @Override
    public boolean supports(@NonNull HttpStatus status) {
        return status == HttpStatus.BAD_REQUEST;
    }

    @Override
    public RestBaseException handle(@NonNull HttpStatusCodeException exception) {
        return new BadRequestException(exception.getResponseBodyAsString(), exception);
    }
}
