package org.example.authservice.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String token) {
        super(token);
    }
}
