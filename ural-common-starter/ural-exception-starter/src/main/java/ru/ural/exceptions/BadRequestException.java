package ru.ural.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RestBaseException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(String message, Throwable throwable) {
        super(message, HttpStatus.BAD_REQUEST, throwable);
    }

    public BadRequestException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public BadRequestException(String message, HttpStatus httpStatus, Throwable throwable) {
        super(message, httpStatus, throwable);
    }
}
