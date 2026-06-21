package com.company.customer.dto;

import java.math.BigDecimal;

public record LoanResponse(
        String loanNumber,
        BigDecimal amount,
        String status
) {}
