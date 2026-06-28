package ru.ural.handlers;

import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import ru.ural.exceptions.RestBaseException;

public interface HttpExceptionHandler {

    boolean supports(@NonNull HttpStatus status);

    RestBaseException handle(@NonNull HttpStatusCodeException exception);

}
