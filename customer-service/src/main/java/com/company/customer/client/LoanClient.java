package com.company.customer.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.company.customer.dto.LoanResponse;



@FeignClient(name= "loan-service")
public interface LoanClient {

	 @GetMapping("/api/loans/customer/{customerId}")
	    public List<LoanResponse> getLoans(@PathVariable Long customerId) ;
	        
	    
}
