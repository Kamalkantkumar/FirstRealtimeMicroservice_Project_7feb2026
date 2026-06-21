package com.company.customer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.company.customer.dto.CustomerDetailsResponse;
import com.company.customer.dto.CustomerRequest;
import com.company.customer.entity.Customer;
import com.company.customer.service.CustomerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
	
	private final CustomerService customerService;
	
	public CustomerController(CustomerService service) {
		this.customerService=service;
	}
	
	//@ResponseStatus(HttpStatus.CREATED)
	/*
	 * Why do you use @ResponseStatus(HttpStatus.CREATED)?
You should say:
For REST API standards, when a resource is created successfully, we return HTTP 201 instead of default 200.
	 */
	@PostMapping("/create")
    public ResponseEntity<Long> createCustomer(@RequestBody CustomerRequest request) {
        
        log.info("REST request received to create a new customer profile.");
        
        Long customerId = customerService.addCustomerDetails(request);
        
        log.info("Successfully processed REST request. Created Customer ID: {}", customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerId);
    }
    
  @GetMapping("/{id}")
    public Customer get(@PathVariable Long id) {
        return customerService.getById(id);
    }

  //using feign client
    @GetMapping("/{id}/details")
    public CustomerDetailsResponse getCustomerDetails(@PathVariable Long id) {
        return customerService.getCustomerDetails(id);
    }
}
