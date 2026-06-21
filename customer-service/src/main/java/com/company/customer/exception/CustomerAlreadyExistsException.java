package com.company.customer.exception;
//We create a specific exception for duplicate users. We map it to 400 Bad Request because the user made a mistake by sending data that already exists.
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// This annotation tells Spring that whenever this exception is thrown, 
// the HTTP status should be 400 Bad Request, not 500 Internal Server Error.
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CustomerAlreadyExistsException extends RuntimeException {

    // Constructor to pass our custom banking message to the superclass (RuntimeException)
    public CustomerAlreadyExistsException(String message) {
        super(message);
    }
}