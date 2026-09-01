package com.nookure.staff.api.exception;

public class DataIntegrityCheckFailedException extends RuntimeException {
    public DataIntegrityCheckFailedException(String message) {
        super(message);
    }

    public DataIntegrityCheckFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
