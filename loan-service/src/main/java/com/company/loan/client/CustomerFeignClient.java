package com.company.loan.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// The 'name' must exactly match the spring.application.name of your Customer Service in Eureka.
@FeignClient(name = "customer-service")
public interface CustomerFeignClient {

    // We only need the HTTP Status code, so returning ResponseEntity<Void> saves memory 
    // by not deserializing the entire Customer JSON object.
    @GetMapping("/api/customers/{customerId}")
    ResponseEntity<Void> verifyCustomerExists(@PathVariable("customerId") Long customerId);
}