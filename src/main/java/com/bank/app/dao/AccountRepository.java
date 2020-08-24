package com.bank.app.dao;

import com.bank.app.entities.Account;

public interface AccountRepository extends TransactionRepository{
		
	public Account findAccountByNumber(Long accountNUmber);
	
	public void update(Account account);
	
}