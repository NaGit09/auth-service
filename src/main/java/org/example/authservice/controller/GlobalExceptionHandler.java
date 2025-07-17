package org.example.authservice.controller;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.example.authservice.exception.InvalidTokenException;
import org.example.authservice.exception.NotFoundException;
import org.example.authservice.utils.GenerateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            InvalidTokenException.class,
            MalformedJwtException.class,
            ExpiredJwtException.class,
            UnsupportedJwtException.class
    })
    public ResponseEntity<?> handleJwtExceptions(Exception ex) {
        return GenerateResponse.generateError
                (401, ex.getMessage());
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException
            (NotFoundException ex) {
        return GenerateResponse.generateError
                (404, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException
            (AccessDeniedException ex) {
        return GenerateResponse.generateError
                (403, "Access denied: " + ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions
            (MethodArgumentNotValidException ex) {

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": "
                        + error.getDefaultMessage())

                .collect(Collectors.joining("; "));

        return GenerateResponse.generateError(400, errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        return GenerateResponse.generateError
                (500, "Internal Server Error: " + ex.getMessage());
    }
}

