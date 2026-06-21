package com.company.loan.dto;

import java.math.BigDecimal;

public record LoanRequest(
        Long customerId,
        String loanType,
        BigDecimal totalAmount
) {}