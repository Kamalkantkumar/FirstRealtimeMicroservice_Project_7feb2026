package com.company.loan.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        String apiPath,
        Integer errorCode,
        String errorMsg,
        LocalDateTime errorTime
) {}