package com.nookure.staff.api.exception;

public class EventHandlerException extends RuntimeException {
    public EventHandlerException(String message) {
        super(message);
    }

    public EventHandlerException(String message, Throwable cause) {
        super(message, cause);
    }
}
