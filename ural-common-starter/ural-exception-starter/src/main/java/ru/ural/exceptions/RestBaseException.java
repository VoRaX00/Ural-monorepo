package ru.ural.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RestBaseException extends RuntimeException {

    private final HttpStatus httpStatus;

    public RestBaseException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public RestBaseException(String message, HttpStatus httpStatus, Throwable throwable) {
        super(message, throwable);
        this.httpStatus = httpStatus;
    }
}
