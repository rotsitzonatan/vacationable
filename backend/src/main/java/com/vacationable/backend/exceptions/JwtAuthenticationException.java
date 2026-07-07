package com.vacationable.backend.exceptions;

public class JwtAuthenticationException extends RuntimeException{
    public JwtAuthenticationException(String message) {
        super(message);
    }
}
