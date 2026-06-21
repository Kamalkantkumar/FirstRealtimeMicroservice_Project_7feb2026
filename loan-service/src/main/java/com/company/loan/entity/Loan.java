package com.company.loan.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false, updatable = false)
	private String loanNumber;

	@Column(nullable = false)
	private Long customerId;

	@Column(nullable = false)
	private String loanType;

	@Column(precision = 15, scale = 2, nullable = false)
	private BigDecimal totalAmount;

	@Column(precision = 15, scale = 2, nullable = false)
	private BigDecimal outstandingAmount;
	
	@Column(nullable = false)
	private String status;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

}
