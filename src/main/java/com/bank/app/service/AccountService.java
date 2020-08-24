package com.bank.app.service;

import java.sql.SQLException;

import com.bank.app.dao.AccountRepository;
import com.bank.app.entities.Account;

public interface AccountService extends AccountRepository {

	// should debit the given amount from the given accountNumber
	public void withdraw(int amount, Long accountNumber) throws SQLException;

	// should credit the given amount from the given accountNumber
	public void deposit(int amount, Long accountNumber) throws SQLException;

	// should insert/create a new account
	public void createAccount(Account account) throws SQLException;

	// should deactivate given account updating the isActive property to false
	public void deActivateAccount(Long accountNumber) throws SQLException;

	// should activate given account updating the isActive property to true
	public void activateAccount(Long accountNumber) throws SQLException;

	// should perform DEBIT and CREDIT operations using the given inputs HINT: REFER
	// account service
	public boolean transfer(Long fromAccount, Long toAccount, int amount) throws SQLException;
}
