package com.bank.app.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data @NoArgsConstructor @AllArgsConstructor @Entity
public class TransactionDetail {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long transactionId;
	@OneToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE})
	private Account account;
	private LocalDate transactionDate;
	private LocalTime transactionTime;
	private int amount;	
	@Enumerated(EnumType.STRING)
	private TransactionType type;
	private int currentBalance;
	
}