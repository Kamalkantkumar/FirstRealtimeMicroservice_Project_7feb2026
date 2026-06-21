package com.company.customer.dto;

import jakarta.validation.constraints.NotBlank;
//Using a record guarantees the incoming data cannot be altered during processing.

public record CustomerRequest(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String nationalIdentityNumber // e.g., PAN, SSN, or Aadhar
) {}