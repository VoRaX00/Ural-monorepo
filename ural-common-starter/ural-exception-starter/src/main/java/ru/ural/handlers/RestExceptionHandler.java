package ru.ural.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.ural.dto.ErrorDto;
import ru.ural.exceptions.RestBaseException;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(RestBaseException.class)
    public ResponseEntity<ErrorDto> handlerError(RestBaseException e) {
        return new ResponseEntity<>(ErrorDto.builder()
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build(),
                e.getHttpStatus()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handlerError(Exception e) {
        log.error("Unknown exception", e);
        return new ResponseEntity<>(ErrorDto.builder()
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
