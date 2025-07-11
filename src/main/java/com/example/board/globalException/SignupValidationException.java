package com.example.board.globalException;

public class SignupValidationException extends RuntimeException {
    public SignupValidationException(String message) {
        super(message);
    }
}