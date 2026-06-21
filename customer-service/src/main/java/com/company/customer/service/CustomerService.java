package com.company.customer.service;

import com.company.customer.dto.CustomerDetailsResponse;
import com.company.customer.dto.CustomerRequest;
import com.company.customer.entity.Customer;

public interface CustomerService {

	Long addCustomerDetails(CustomerRequest request);
	
    Customer getById(Long id);
    //using feign client
    public CustomerDetailsResponse getCustomerDetails(Long customerId);
}