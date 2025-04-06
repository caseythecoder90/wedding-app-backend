package com.wedding.backend.wedding_app.exception;

public class DatabaseException extends RuntimeException {

    final Exception exception;
    final String errorKey;

    public DatabaseException(Exception exception, String errorMessage, String errorKey) {
        super(errorMessage);
        this.exception = exception;
        this.errorKey = errorKey;
    }
}
