package com.company.loan.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.loan.dto.LoanRequest;
import com.company.loan.dto.LoanResponse;
import com.company.loan.service.LoanService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

	private final LoanService loanService;

	@GetMapping("/customer/{customerId}")
	public ResponseEntity<List<LoanResponse>> getLoans(@PathVariable Long customerId) {
		log.info("REST request received to fetch loans for Customer ID: {}", customerId);

		List<LoanResponse> loans = loanService.getLoansByCustomerId(customerId);

		log.info("Returning {} loan records for Customer ID: {}", loans.size(), customerId);
		return ResponseEntity.ok(loans);
	}

	@PostMapping("/create")
	public ResponseEntity<String> addLoanDetails(@RequestBody LoanRequest request) {

		log.info("REST request received to create a new loan for Customer ID: {}", request.customerId());

		String loanNumber = loanService.addLoanDetails(request);

		log.info("Successfully processed REST request. Created Loan Number: {}", loanNumber);
		return ResponseEntity.status(HttpStatus.CREATED).body(loanNumber); // Returns HTTP 201 CREATED
	}

}