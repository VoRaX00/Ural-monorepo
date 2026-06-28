package ru.ural.exceptions;

import org.springframework.http.HttpStatus;

public class ConflictException extends RestBaseException {
    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public ConflictException(String message, Throwable throwable) {
        super(message, HttpStatus.CONFLICT, throwable);
    }

    public ConflictException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public ConflictException(String message, HttpStatus httpStatus, Throwable throwable) {
        super(message, httpStatus, throwable);
    }
}
