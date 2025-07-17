package org.example.authservice.exception;

public class PasswordNotMatchException extends RuntimeException {
    public PasswordNotMatchException(String password) {
        super(password + " not match ");
    }
}
