package com.company.customer.dto;

import java.util.List;

public record CustomerDetailsResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String nationalIdentityNumber,
        List<LoanResponse> loans
) {}
