package com.company.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.customer.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	//We add a method to quickly check if a customer already exists before trying to save them.
	boolean existsByEmailOrNationalIdentityNumber(String email, String nationalIdentityNumber);
	
}
