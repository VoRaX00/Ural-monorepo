package ru.ural.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RestBaseException {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public NotFoundException(String message, Throwable throwable) {
        super(message, HttpStatus.NOT_FOUND, throwable);
    }

    public NotFoundException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public NotFoundException(String message, HttpStatus httpStatus, Throwable throwable) {
        super(message, httpStatus, throwable);
    }
}
