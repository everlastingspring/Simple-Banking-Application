package com.bank.app.BankApp;

import java.sql.SQLException;
import java.util.Scanner;

import com.bank.app.entities.Account;
import com.bank.app.entities.Customer;
import com.bank.app.service.AccountService;
import com.bank.app.service.Services;

/**
 * COMPANY BANK!
 *
 */
public class App {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		AccountService service = new Services();
		Integer choice = 0;
		String s = "0";

		System.out.println("WELCOME TO COMPANY BANK\n\n");

		while (true) {
			System.out.print("press 1 for login:\n");
			System.out.print("press 2 for create your account:\n");
			System.out.println("press 3 for exit.\n\n");

			while (!scanner.hasNext("[1-3]")) {
				System.out.println("please enter a number from 1 to 3\n");
				s = scanner.next();
			}
			s = scanner.next();
			choice = Integer.parseInt(s);

			if (choice == 1) {

				System.out.println("enter account number");
				while (true) {
					if (!scanner.hasNextInt()) {
						System.out.println("please enter a valid account number \n");
						s = scanner.next();
					} else {
						s = scanner.next();
						break;
					}
				}

				Account account = service.findAccountByNumber(Long.parseLong(s));

				if (account != null) {
					if (account.isActive()) {
						while (true) {
							System.out.print("press 1 for view account deatils:\n");
							System.out.print("press 2 for check balance:\n");
							System.out.print("press 3 for view statement:\n");
							System.out.print("press 4 for withdraw amount:\n");
							System.out.print("press 5 for deposit amount:\n");
							System.out.print("press 6 for transfer amount from account to account/NEFT:\n");
							System.out.print("press 7 for deactivate account:\n");
							System.out.print("press 8 for go back:\n");
							System.out.println("press 9 for exit.\n\n");

							while (!scanner.hasNext("[1-9]")) {
								System.out.println("please enter a number from 1 to 9 only\n");
								s = scanner.next();
							}
							s = scanner.next();
							choice = Integer.parseInt(s);

							if (choice == 1) {
									System.out.println("account details: \n" + account + "\n\n");
								
							} else if (choice == 2) {
									System.out.println("account details: \n" + account.getBalance() + "\n\n");
							} else if (choice == 3) {
								try {
										System.out.println(service.getAllTransactionDetailsByAccount(account) + "\n\n");
								} catch (SQLException e) {
									e.printStackTrace();
								}
							} else if (choice == 4) {
								System.out.println("enter the amount to withdraw:\n");
								try {
									service.withdraw(scanner.nextInt(), account.getAccountNumber());
								} catch (SQLException e) {
									e.printStackTrace();
								}
							} else if (choice == 5) {
								System.out.println("enter the amount to deposit:\n");
								try {
									service.deposit(scanner.nextInt(), account.getAccountNumber());
								} catch (SQLException e) {
									e.printStackTrace();
								}
							} else if (choice == 6) {
								System.out.println("enter the account number to transfer:\n");
								long toNumber = scanner.nextLong();
								System.out.println("enter the amount to transfer ");
								try {
									service.transfer(account.getAccountNumber(), toNumber, scanner.nextInt());
								} catch (SQLException e) {
									e.printStackTrace();
								}
							} else if (choice == 7) {
								try {
									service.deActivateAccount(account.getAccountNumber());
								} catch (SQLException e) {
									e.printStackTrace();
								}
							} else if (choice == 8) {
								break;
							} else {
								System.out.println("\nexited successfully");
								System.exit(0);
							}
						}

					} else {
						while (true) {
							System.out.print("press 1 for view account deatils:\n");
							System.out.print("press 2 for activate account:\n");
							System.out.print("press 3 for go back:\n");
							System.out.println("press 4 for exit.\n\n");

							while (!scanner.hasNext("[1-4]")) {
								System.out.println("please enter a number from 1 to 9 only\n");
								s = scanner.next();
							}
							s = scanner.next();
							choice = Integer.parseInt(s);

							if (choice == 1) {
								System.out.println(account);
							} else if (choice == 2) {
								try {
									service.activateAccount(account.getAccountNumber());
									System.out.println("\n your account is now active you can go back\n");
								} catch (SQLException e) {
									System.err.println("\n problem activating account\n");
									e.printStackTrace();
								}
							} else if (choice == 3) {
								break;
							} else {
								System.out.println("\n exited successfully");
								System.exit(0);
							}
						}
					}
				} else {
					System.out.println("\n no account exists with given account number\n");
				}
			}

			else if (choice == 2) {
				Account account = new Account();
				Customer customer = new Customer();
				System.out.println("enter name");
				account.setName(scanner.next());
				System.out.println("enter account number");
				account.setAccountNumber(scanner.nextLong());
				System.out.println("enter the initial balance to have ");
				account.setBalance(scanner.nextInt());
				System.out.println("enter the emailAddress");
				customer.setEmailAddress(scanner.next());
				System.out.println("enter your city");
				customer.setCity(scanner.next());
				System.out.println("enter your country");
				customer.setCountry(scanner.next());
				account.setActive(true);
				account.setCustomer(customer);

				try {
					service.createAccount(account);
					System.out.println("\n account successfully created\n");
				} catch (SQLException e) {
					System.err.println("\n account is not created\n");
					e.printStackTrace();
				}

			} else {
				System.out.println("\n exited successfully");
				System.exit(0);
				break;
			}
		}
		scanner.close();
	}
}
