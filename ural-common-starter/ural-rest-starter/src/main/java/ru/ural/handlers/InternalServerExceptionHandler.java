package ru.ural.handlers;

import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import ru.ural.exceptions.InternalServerException;
import ru.ural.exceptions.RestBaseException;

public class InternalServerExceptionHandler implements HttpExceptionHandler {
    @Override
    public boolean supports(@NonNull HttpStatus status) {
        return status == HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    public RestBaseException handle(@NonNull HttpStatusCodeException exception) {
        return new InternalServerException(exception.getResponseBodyAsString(), exception);
    }
}
