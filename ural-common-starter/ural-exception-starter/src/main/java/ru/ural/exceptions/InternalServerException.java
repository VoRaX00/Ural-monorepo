package ru.ural.exceptions;

import org.springframework.http.HttpStatus;

public class InternalServerException extends RestBaseException {
    public InternalServerException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public InternalServerException(String message, Throwable throwable) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, throwable);
    }

    public InternalServerException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public InternalServerException(String message, HttpStatus httpStatus, Throwable throwable) {
        super(message, httpStatus, throwable);
    }
}
