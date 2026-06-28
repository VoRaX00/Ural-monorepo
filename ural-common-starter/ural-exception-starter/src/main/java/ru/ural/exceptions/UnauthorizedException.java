package ru.ural.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends RestBaseException {

    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedException(String message, Throwable throwable) {
        super(message, HttpStatus.UNAUTHORIZED, throwable);
    }

    public UnauthorizedException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public UnauthorizedException(String message, HttpStatus httpStatus, Throwable throwable) {
        super(message, httpStatus, throwable);
    }

}
