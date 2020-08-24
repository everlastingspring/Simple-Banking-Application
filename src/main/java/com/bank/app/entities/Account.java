package com.bank.app.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data @NoArgsConstructor @AllArgsConstructor @Entity
public class Account {

	@Id 
	private Long accountNumber;
	@OneToOne(fetch = FetchType.EAGER,cascade = {CascadeType.MERGE,CascadeType.REMOVE})
	private Customer customer;
	private String name;
	private boolean isActive;
	private int balance;

	public void debit(int amount) {
		balance -= amount;
	}

	public void credit(int amount) {
		balance += amount;
	}
}