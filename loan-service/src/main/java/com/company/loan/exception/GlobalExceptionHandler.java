package com.company.loan.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handle our custom ResourceNotFoundException (Returns 404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException exception, WebRequest webRequest) {

        log.warn("Resource Not Found: {}", exception.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // 2. Handle ANY other unexpected exception (Returns 500)
    // SECURITY CRITICAL: Never return the raw exception.getMessage() to the client!
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception exception, WebRequest webRequest) {

        // Log the actual error stack trace for internal IT debugging
        log.error("CRITICAL SYSTEM ERROR: {}", exception.getMessage(), exception);

        // Return a sanitized, generic message to the API consumer
        ErrorResponse errorResponse = new ErrorResponse(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected system error occurred. Please contact banking support.",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    //Add this new block to your existing GlobalExceptionHandler so it returns a proper HTTP 503 code.when customer-service is down. This will allow the API consumer to understand that the issue is with the external service and not with their request.
    @ExceptionHandler(ExternalServiceDownException.class)
    public ResponseEntity<ErrorResponse> handleExternalServiceDownException(
            ExternalServiceDownException exception, WebRequest webRequest) {

        log.error("CIRCUIT BREAKER OPEN: {}", exception.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                webRequest.getDescription(false),
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                exception.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }
}