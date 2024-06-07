package com.progressSoft.exception;

public class DealAlreadyExistsException extends RuntimeException {
    public DealAlreadyExistsException(String message) {
        super(message);
    }
}