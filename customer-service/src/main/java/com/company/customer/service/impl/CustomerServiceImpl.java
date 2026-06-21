package com.company.customer.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.customer.client.LoanClient;
import com.company.customer.dto.CustomerDetailsResponse;
import com.company.customer.dto.CustomerRequest;
import com.company.customer.dto.LoanResponse;
import com.company.customer.entity.Customer;
import com.company.customer.exception.CustomerAlreadyExistsException;
import com.company.customer.repository.CustomerRepository;
import com.company.customer.service.CustomerService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
	
	
	private final CustomerRepository customerRepository;
    private final LoanClient loanClient;
    
    //We check for duplicates first. Also, notice that we do not log the customer's name, email, or national ID to prevent security breaches.
    @Override
    @Transactional
    public Long addCustomerDetails(CustomerRequest request) {
        log.info("Processing new customer registration request.");

     // 1. Prevent duplicate accounts by checking the database first
        if (customerRepository.existsByEmailOrNationalIdentityNumber(request.email(), request.nationalIdentityNumber())) {
            
            log.warn("Registration failed: Customer with provided identity already exists.");
            
            // 👇 CHANGED THIS LINE to throw our custom banking exception
            throw new CustomerAlreadyExistsException("Registration Failed: A user already exists with the provided Email or National Identity Number."); 
        }

        // 2. Build the entity
        Customer newCustomer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .nationalIdentityNumber(request.nationalIdentityNumber())
                .createdAt(LocalDateTime.now())
                .build();

        // 3. Save to database
        Customer savedCustomer = customerRepository.save(newCustomer);
        
        log.info("Successfully created new customer. Assigned Customer ID: {}", savedCustomer.getId());

        return savedCustomer.getId();
    }
    

    

    @Override
    public Customer getById(Long id) {

        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Customer Not Found for " + id + " id, Please Enter Correct Id"));
    }

    // 🔴 Circuit Breaker added here
    @Override
    @CircuitBreaker(name = "loanService", fallbackMethod = "loanServiceFallback")
    @Retry(name = "loanService")
    @RateLimiter(name = "loanService")
    public CustomerDetailsResponse getCustomerDetails(Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Feign call
        List<LoanResponse> loans = loanClient.getLoans(customerId);

        return new CustomerDetailsResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getNationalIdentityNumber(),
                loans);
    }

    // 🟢 Fallback method
    public CustomerDetailsResponse loanServiceFallback(Long customerId, Throwable ex) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        System.out.println("Loan Service is DOWN : " + ex.getMessage());

        return new CustomerDetailsResponse(
        		 customer.getId(),
                 customer.getFirstName(),
                 customer.getLastName(),
                 customer.getEmail(),
                 customer.getPhoneNumber(),
                 customer.getNationalIdentityNumber(),
                List.of()   // empty loans
        );
    }
	
	
	

	
	
//i have commented because i have implemented circuitbreaker pattern in same code above
	/*
	private final CustomerRepository repository;
	private final LoanClient loanClient;

	
	
	
	@Override
	public Customer create(CustomerRequest request) {
		Customer customer = new Customer();
		customer.setFullName(request.getFullName());
		customer.setEmail(request.getEmail());
		customer.setMobile(request.getMobile());
		return repository.save(customer);
	}

	@Override
	public Customer getById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new RuntimeException("Customer Not Found for" + id + " id , Please Enter Currect Id "));

	}
	 //using feign client
	@Override
	public CustomerDetailsResponse getCustomerDetails(Long customerId) {
		Customer customer=repository.findById(customerId).orElseThrow();
		
		List <LoanResponse> loans=loanClient.getLoans(customerId);
		
		return new CustomerDetailsResponse(customer.getId(), customer.getFullName(), customer.getEmail(), loans);
		
	}
*/
}
