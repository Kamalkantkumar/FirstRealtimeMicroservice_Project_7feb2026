package com.company.loan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.loan.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {
	List<Loan> findByCustomerId(Long customerId);
}
