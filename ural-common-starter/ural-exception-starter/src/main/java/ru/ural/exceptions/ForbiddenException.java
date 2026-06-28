package ru.ural.exceptions;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends RestBaseException {

    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }

    public ForbiddenException(String message, Throwable throwable) {
        super(message, HttpStatus.FORBIDDEN, throwable);
    }

    public ForbiddenException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public ForbiddenException(String message, HttpStatus httpStatus, Throwable throwable) {
        super(message, httpStatus, throwable);
    }

}
