package com.company.loan.dto;

import java.math.BigDecimal;

public record LoanResponse(
        String loanNumber,
        BigDecimal amount,
        String status
) {}