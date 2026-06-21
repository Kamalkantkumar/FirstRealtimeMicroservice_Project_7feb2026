package com.company.loan.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.company.loan.client.CustomerFeignClient;
import com.company.loan.dto.LoanRequest;
import com.company.loan.dto.LoanResponse;
import com.company.loan.entity.Loan;
import com.company.loan.exception.ExternalServiceDownException;
import com.company.loan.exception.ResourceNotFoundException;
import com.company.loan.repository.LoanRepository;
import com.company.loan.service.LoanService;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

	private final LoanRepository loanRepository;
	private final CustomerFeignClient customerClient; // 👇 Inject the Feign Client

	/*
	 * We add the @CircuitBreaker annotation. Notice how we removed the generic
	 * catch (FeignException e) from earlier. We want the connection failure to
	 * bubble up so Resilience4j can catch it and trigger the fallback!
	 */
	@Override
    @Transactional(readOnly = true)
    // 👇 ADDED THIS ANNOTATION
    @CircuitBreaker(name = "customerServiceBreaker", fallbackMethod = "getLoansFallback")
    public List<LoanResponse> getLoansByCustomerId(Long customerId) {
        log.info("Initiating cross-service validation for Customer ID: {}", customerId);

        try {
            customerClient.verifyCustomerExists(customerId);
        } catch (FeignException.NotFound e) {
            log.warn("Security/Validation Failure: Customer ID {} does not exist.", customerId);
            throw new ResourceNotFoundException("Customer", "customerId", customerId.toString());
        }
        // Notice we no longer catch generic Feign exceptions here.
        // If the connection refuses, Resilience4j intercepts it automatically!

        List<Loan> loanEntities = loanRepository.findByCustomerId(customerId);
        
        return loanEntities.stream()
                .map(loan -> new LoanResponse(
                        loan.getLoanNumber(),
                        loan.getTotalAmount(),
                        loan.getStatus()
                ))
                .toList();
    }
	
	// 👇 ADDED THIS FALLBACK METHOD
    // The signature must perfectly match the original method, plus a Throwable parameter.
    public List<LoanResponse> getLoansFallback(Long customerId, Throwable throwable) {
        log.error("CIRCUIT BREAKER TRIGGERED! Target service is offline. Error: {}", throwable.getMessage());
        
        // This clean message will be sent to the GlobalExceptionHandler and returned to the UI
        throw new ExternalServiceDownException("customer-service/Identity verification system is currently undergoing maintenance. Please try again later.");
    }

	@Override
	@Transactional
	public String addLoanDetails(LoanRequest request) {
		log.info("Processing new loan request for Customer ID: {}", request.customerId());

		String generatedLoanNumber = "LN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

		Loan newLoan = Loan.builder().loanNumber(generatedLoanNumber).customerId(request.customerId())
				.loanType(request.loanType()).totalAmount(request.totalAmount())
				.outstandingAmount(request.totalAmount()).status("APPROVED").createdAt(LocalDateTime.now()).build();

		loanRepository.save(newLoan);

		log.info("Successfully created loan record with Loan Number: {}", generatedLoanNumber);

		return generatedLoanNumber;
	}

}
