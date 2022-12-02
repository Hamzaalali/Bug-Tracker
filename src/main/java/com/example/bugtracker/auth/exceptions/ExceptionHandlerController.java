package com.example.bugtracker.auth.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;


@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleException(MethodArgumentNotValidException ex) {
        String error=ex.getBindingResult().getAllErrors().get(0).unwrap(ConstraintViolation.class).getMessage();
        ErrorDto dto = new ErrorDto(HttpStatus.BAD_REQUEST, error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);

    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> handleException(RuntimeException ex) {
        ErrorDto dto = new ErrorDto(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }
    @ExceptionHandler(MyCustomExceptions.class)
    public ResponseEntity<ErrorDto> handleException(MyCustomExceptions ex) {
        ErrorDto dto = new ErrorDto(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }
    @Data
    public static class ErrorDto {
        private final int status;
        private final String error;
        private final String message;

        public ErrorDto(HttpStatus httpStatus, String message) {
            status = httpStatus.value();
            error = httpStatus.getReasonPhrase();
            this.message = message;
        }

    }

}