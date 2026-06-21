package com.company.loan.service;

import java.util.List;

import com.company.loan.dto.LoanRequest;
import com.company.loan.dto.LoanResponse;

public interface LoanService {
	 List<LoanResponse> getLoansByCustomerId(Long customerId);

	String addLoanDetails(LoanRequest request);
}
