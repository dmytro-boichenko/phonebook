package com.boichenko.phonebook.exception;

public class TokenVerificationException extends RuntimeException {

    public TokenVerificationException() {
    }

    public TokenVerificationException(String message) {
        super(message);
    }

    public TokenVerificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenVerificationException(Throwable cause) {
        super(cause);
    }

    public TokenVerificationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
