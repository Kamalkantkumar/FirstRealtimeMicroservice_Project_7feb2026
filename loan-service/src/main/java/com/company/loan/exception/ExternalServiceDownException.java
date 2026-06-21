package com.company.loan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Create a new exception specifically for when external banking systems are down.

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class ExternalServiceDownException extends RuntimeException {

    public ExternalServiceDownException(String message) {
        super(message);
    }
}
