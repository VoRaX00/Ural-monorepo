package ru.ural.handlers;

import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import ru.ural.exceptions.ConflictException;
import ru.ural.exceptions.RestBaseException;

public class ConflictHandler implements HttpExceptionHandler {
    @Override
    public boolean supports(@NonNull HttpStatus status) {
        return status == HttpStatus.CONFLICT;
    }

    @Override
    public RestBaseException handle(@NonNull HttpStatusCodeException exception) {
        return new ConflictException(exception.getResponseBodyAsString(), exception);
    }
}
