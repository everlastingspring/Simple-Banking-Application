package com.bank.app.dao;

import java.sql.SQLException;
import java.util.List;

import com.bank.app.entities.Account;
import com.bank.app.entities.TransactionDetail;

public interface TransactionRepository {

	public void addTransaction(TransactionDetail transactionDetail) throws SQLException;
	
	public List<TransactionDetail> getAllTransactionDetailsByAccount(Account account) throws SQLException;
}

