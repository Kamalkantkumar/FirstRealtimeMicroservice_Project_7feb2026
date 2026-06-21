package com.company.customer.exception;

import java.time.LocalDateTime;
//This ensures the UI gets the exact same JSON format from both the loan-service and the customer-service.
//We use a Java record here because error messages should be immutable (cannot be changed once created).
//This is a banking standard to prevent data tampering in the logs.
public record ErrorResponse(
     String apiPath,
     Integer errorCode,
     String errorMsg,
     LocalDateTime errorTime
) {}