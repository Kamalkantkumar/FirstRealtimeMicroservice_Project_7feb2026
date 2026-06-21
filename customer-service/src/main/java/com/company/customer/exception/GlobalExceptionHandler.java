package com.company.customer.exception;
//This is the "PR Department" for your customer-service. It catches the error and formats it perfectly.
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Slf4j
// @RestControllerAdvice makes this class act as an interceptor for all controllers in this microservice.
// It catches exceptions before they reach the user.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // This method specifically catches our new CustomerAlreadyExistsException
    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCustomerAlreadyExistsException(
            CustomerAlreadyExistsException exception, WebRequest webRequest) {

        // Log the warning for internal banking audit purposes
        log.warn("Registration Blocked: {}", exception.getMessage());

        // Create the clean, standardized JSON response for the frontend UI
        ErrorResponse errorResponse = new ErrorResponse(
                webRequest.getDescription(false), // Gets the API path (e.g., /api/customers/create)
                HttpStatus.BAD_REQUEST.value(),   // Sets error code to 400
                exception.getMessage(),           // Injects our custom message
                LocalDateTime.now()               // Stamps the exact time of the error
        );

        // Return the clean JSON to Postman
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    // Fallback for any other unexpected system crashes
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception exception, WebRequest webRequest) {

        log.error("CRITICAL SYSTEM ERROR: {}", exception.getMessage(), exception);

        ErrorResponse errorResponse = new ErrorResponse(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected system error occurred in the Customer Service. Please contact support.",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}