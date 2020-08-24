package com.bank.app.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.bank.app.entities.Account;
import com.bank.app.entities.TransactionDetail;
import com.bank.app.entities.TransactionType;

public class Services implements AccountService {

	EntityManagerFactory factory = Persistence.createEntityManagerFactory("bank");

	public void addTransaction(TransactionDetail transactionDetail) throws SQLException {
		EntityManager manager = factory.createEntityManager();
		try {
			manager.getTransaction().begin();
			manager.persist(transactionDetail);
			manager.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manager.close();
		}
	}

	public List<TransactionDetail> getAllTransactionDetailsByAccount(Account account) throws SQLException {
		EntityManager manager = factory.createEntityManager();
		List<TransactionDetail> transactionDetails = new ArrayList<TransactionDetail>();
		try {
			TypedQuery<TransactionDetail> result = manager.createQuery(
					"select t from TransactionDetail t where t.account.accountNumber=?1",
					TransactionDetail.class);
			result.setParameter(1, account.getAccountNumber());
			transactionDetails = result.getResultList();
			if(findAccountByNumber(account.getAccountNumber()).isActive()) {
				if (transactionDetails !=null) {
					return transactionDetails;
				} else {
					System.err.println("no transactions involved in your account\n");
				}
			}else {
				System.err.println("your account is not active\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manager.close();
		}
		return null;
	}

	public Account findAccountByNumber(Long accountNUmber) {
		EntityManager manager = factory.createEntityManager();
		try {
			Account account = manager.find(Account.class, accountNUmber);
			if (account != null) {
				return account;
			} else {
				System.err.println("invalid account number");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manager.close();
		}
		return null;
	}

	public void update(Account account) {
		EntityManager manager = factory.createEntityManager();
		try {
			manager.getTransaction().begin();
			Account accountByNumber = findAccountByNumber(account.getAccountNumber());
			if(accountByNumber.getCustomer().getCity()!=null||accountByNumber.getCustomer().getCountry()!=null) {
				manager.persist(account);
				manager.flush();
				manager.clear();
			}else if(account.getCustomer().getEmailAddress()!=null) {
				manager.persist(account);
				manager.flush();
			}
			manager.getTransaction().commit();
		} catch (Exception e) {
			manager.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			manager.close();
		}

	}

	public void withdraw(int amount, Long accountNumber) throws SQLException {
		EntityManager manager = factory.createEntityManager();
		try {
			Account account = findAccountByNumber(accountNumber);
			if (account.isActive()) {
				if(account.getBalance()>=amount) {
					manager.getTransaction().begin();
					account.debit(amount);
					manager.merge(account);
					TransactionDetail detail = new TransactionDetail();
					detail.setAccount(account);
					detail.setAmount(amount);
					detail.setCurrentBalance(account.getBalance());
					detail.setTransactionDate(LocalDate.now());
					detail.setTransactionTime(LocalTime.now());
					detail.setType(TransactionType.DEBIT);
					addTransaction(detail);
					manager.getTransaction().commit();

					System.out.println("amount withdrawn in Rs: " + amount);
				}
				else {
					System.err.println("insufficient funds");
				}

				
			} else {
				System.err.println("your account is deactivated- Activate your account");
			}
		} catch (Exception e) {
			System.err.println("amount withdraw gone fail: amount not withdrawn");
			manager.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			manager.close();
		}
	}

	public void deposit(int amount, Long accountNumber) throws SQLException {
		EntityManager manager = factory.createEntityManager();
		try {
			Account account = findAccountByNumber(accountNumber);
			if (account.isActive()) {
				manager.getTransaction().begin();
				account.credit(amount);
				manager.merge(account);
				TransactionDetail detail = new TransactionDetail();
				detail.setAccount(account);
				detail.setAmount(amount);
				detail.setCurrentBalance(account.getBalance());
				detail.setTransactionDate(LocalDate.now());
				detail.setTransactionTime(LocalTime.now());
				detail.setType(TransactionType.CREDIT);
				addTransaction(detail);
				manager.getTransaction().commit();
				System.out.println("deposited successfully");

			} else {
				System.err.println("your account is deactivated- Activate your account");
			}
		} catch (Exception e) {
			System.err.println("deposit amount gone fail: amount not deposited");
			manager.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			manager.close();
		}
	}

	public void createAccount(Account account) throws SQLException {
		EntityManager manager = factory.createEntityManager();
		try {
			manager.getTransaction().begin();
			manager.persist(account.getCustomer());
			manager.persist(account);
			manager.getTransaction().commit();
		} catch (Exception e) {
			System.err.println("ERROR:---------account not created");
			manager.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			manager.close();
		}
	}

	public void deActivateAccount(Long accountNumber) throws SQLException {
		EntityManager manager = factory.createEntityManager();
		try {
			Account account = findAccountByNumber(accountNumber);
			if (!account.isActive()) {
				System.err.println("account is not active: activate the account");
			} else {
				manager.getTransaction().begin();
				account.setActive(false);
				manager.merge(account);
				manager.getTransaction().commit();
				System.out.println("account deacivated");
			}
		} catch (Exception e) {
			System.err.println("account is not deactivated");
			manager.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			manager.close();
		}
	}

	public void activateAccount(Long accountNumber) throws SQLException {
		EntityManager manager = factory.createEntityManager();
		try {
			Account account = findAccountByNumber(accountNumber);
			if (account.isActive()) {
				System.err.println("account already active");
			} else {
				manager.getTransaction().begin();
				account.setActive(true);
				manager.merge(account);
				manager.getTransaction().commit();
			}
		} catch (Exception e) {
			System.err.println("ERROR:-------account is not activated");
			manager.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			manager.close();
		}
	}

	public boolean transfer(Long fromAccount, Long toAccount, int amount) throws SQLException {
		EntityManager manager = factory.createEntityManager();
		try {
			Account destAccount = findAccountByNumber(toAccount);
			if (destAccount != null && destAccount.isActive()) {
				Account sourceAccount = findAccountByNumber(fromAccount);

				sourceAccount.debit(amount);
				destAccount.credit(amount);
				
				manager.getTransaction().begin();
				manager.merge(sourceAccount);
				TransactionDetail detail = new TransactionDetail();
				detail.setAccount(sourceAccount);
				detail.setAmount(amount);
				detail.setCurrentBalance(sourceAccount.getBalance());
				detail.setTransactionDate(LocalDate.now());
				detail.setTransactionTime(LocalTime.now());
				detail.setType(TransactionType.DEBIT);
				addTransaction(detail);
				manager.merge(destAccount);
				TransactionDetail detail2 = new TransactionDetail();
				detail2.setAccount(destAccount);
				detail2.setAmount(amount);
				detail2.setCurrentBalance(destAccount.getBalance());
				detail2.setTransactionDate(LocalDate.now());
				detail2.setTransactionTime(LocalTime.now());
				detail2.setType(TransactionType.CREDIT);
				addTransaction(detail2);
				manager.getTransaction().commit();
				return true;
			} else {
				System.err.println("entered account number does not exist");
			}
		} catch (Exception e) {
			System.err.println("amount not transfered");
			manager.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			manager.close();
		}
		return false;
	}
}
