package com.revature.projectzero.main;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.projectzero.accounts.Account;
import com.revature.projectzero.accounts.AccountTypes;
import com.revature.projectzero.accounts.User;
import com.revature.projectzero.db.BankDB;
import com.revature.projectzero.db.DatabaseSingleton;

public class Program {
	private static ProgramState state = ProgramState.mainLoop;
	private static User u;
	final static Logger log = Logger.getLogger(Program.class);
	private static boolean done = false;
	
	public static boolean isDone() {
		return done;
	}
	
	public static ProgramState getState() {
		return state;
	}
	public static void start() {
		DatabaseSingleton db = BankDB.getInstance();
		do {
			switch (state) {
			case mainLoop:
				mainLoop(System.out);
				break;
			case userLoginLoop:
				userLoginLoop(System.out, db);
				break;
			case userLoggedInLoop:
				userLoggedInLoop(System.out, db);
				break;
			case activateAccountLoop:
				createAccountLoop(System.out, db);
				break;
			case createAccountLoop:
				createAccountLoop(System.out, db);
				break;
			default:
				done = true;
				break;
			}

		} while (!done);

	}
	
	public static void mainLoop(PrintStream out) {
		log.debug("mainLoop");
		out.println("Welcome to Ipsum Bank!\n" + "Options are:\n" + "enter \"login\" to log into an account\n"
				+ "enter \"createAccount\" to create an account\n" + "enter \"quit\" to quit the application\n");
		Scanner in = new Scanner(System.in);
		String input = in.nextLine();
		if (input.equalsIgnoreCase("login")) {
			state = ProgramState.userLoginLoop;
		} else if (input.equalsIgnoreCase("createaccount")) {
			state = ProgramState.createAccountLoop;
		} else if (input.equalsIgnoreCase("quit")) {
			done = true;
		}
	}

	public static void createAccountLoop(PrintStream out, DatabaseSingleton db) {
		log.debug("createAccountLoop");
		out.println("Please enter the type of account you wish to create");
		out.println("enter \"useraccount\" to create a user account");
		out.println("enter \"adminaccount\" to create a admin account");
		out.println("enter \"quit\" to go back to the main menu\n");
		Scanner in = new Scanner(System.in);
		String input = in.nextLine();
		boolean isAdmin = false;
		if (input.equalsIgnoreCase("useraccount")) {
			isAdmin = false;
		} else if (input.equalsIgnoreCase("adminaccount")) {
			isAdmin = true;
		} else if (input.equalsIgnoreCase("quit")) {
			state = ProgramState.mainLoop;
			return;
		} else {
			out.println("Input not understood");
			return;
		} // ELSE

		boolean isUniqueUsername = true;
		String username = null;
		do {
			out.println("Enter username:");
			username = in.nextLine();
			isUniqueUsername = db.isUniqueUsername(username);
			if (!isUniqueUsername) {
				out.println("User name taken please pick another!");
				continue;
			}  //ELSE
			break;
		} while (true);
		out.println("Username not taken!");
		out.println("Enter Name:");
		String name = in.nextLine ();
		out.println("Enter Last Name:");
		String lname = in.nextLine();
		out.println("Enter Password:");
		String password = in.nextLine();
		db.create(new User(name, lname, username, isAdmin, false, false, 0L),password);
		out.println("Account Creation Successful");
		state = ProgramState.mainLoop;
	}

	public static void userLoggedInLoop(PrintStream out, DatabaseSingleton db) {
		log.debug("userLoggedInLoop");
		out.println("Welcome " + u.getName());
		if (u.isAdmin()) {
			out.println("enter \"activateAccounts\" to activate accounts");
		}
		out.println("enter \"deposit\" to deposit money on a given account");
		out.println("enter \"withdraw\" to withdraw from a given account");
		out.println("enter \"createbankaccount\" to create bank accounts");
		out.println("enter \"deletebankaccount\" to create bank accounts");
		out.println("enter \"quit\" to go back to the main menu\n");
		Scanner in = new Scanner(System.in);
		String input = in.nextLine();
		if (input.equalsIgnoreCase("activateAccounts")) {
			int page = 0;
			do {
				List<User> list = db.getUnActivatedAccounts(page);
				
				out.println("The following Accounts need activation");
				try {
					for (int i = 0; i < list.size(); i++) {
						out.println(i + ") " + list.get(i));
					}
				} catch (NullPointerException e) {
					log.error("Null pointer exception inside of userLoggedInLoop");
				}
				out.println("Enter the number of account to activate or conversely enter quit");
				out.println("You can view more accounts by typing \"nextpage\" \"previouspage\"");
				input = in.nextLine();
				if (input.equalsIgnoreCase("quit"))
					break;
				else if(input.equalsIgnoreCase("previouspage")) {
					page=page==0?0:page-1;
					continue;
				}
				else if (input.equalsIgnoreCase("nextPage")) {
					page++;
					continue;
				}
				try {
					int accountIndex = Integer.parseInt(input);
					list.get(accountIndex).setActive(true);
					out.println("Updating account");
					db.update(list.get(accountIndex));
				} catch (NumberFormatException e) {
					log.error("ClassCastException pointer exception inside of userLoggedInLoop");
					out.println("Not a number entered");
				} catch (NullPointerException e) {
					log.error("Null pointer exception inside of userLoggedInLoop");
				}
			} while (true);

		} else if (input.equalsIgnoreCase("deposit") || input.equalsIgnoreCase("withdraw")) {
			int transactionSign = 1;
			if (input.equalsIgnoreCase("withdraw"))
				transactionSign = -1;
			int page=0;
			do {
				try {
					getUserAccounts(db,page);
					out.println("You have the following accounts:");
					for (int i = 0; i < u.getAccounts().size(); i++) {
						Account account = u.getAccounts().get(i);
						out.println(i + ") " + account);
					}
					out.println("Enter the account index number or conversely enter \"quit\"");
					out.println("You can view more accounts by typing \"nextpage\" \"previouspage\"");
					input = in.nextLine();
					if (input.equalsIgnoreCase("quit"))
						break;
					else if(input.equalsIgnoreCase("previouspage")) {
						page=page==0?0:page-1;
						continue;
					}
					else if (input.equalsIgnoreCase("nextPage")) {
						page++;
						continue;
					}
					try {
						int accountIndex = Integer.parseInt(input);
						Account a = u.getAccounts().get(accountIndex);
						out.println("Enter the amount or conversely enter \"quit\"");
						input = in.nextLine();
						if (input.equalsIgnoreCase("quit"))
							continue;
						try {
							double amount = Double.parseDouble(input);
							a.setBalance(a.getBalance() + (transactionSign * amount));
							db.update(a);
						} catch (NumberFormatException e) {
							log.error("ClassCastException pointer exception inside of userLoggedInLoop");
							out.println("Not a number entered");
						} catch (NullPointerException e) {
							log.error("Null pointer exception inside of userLoggedInLoop");
						}
					} catch (NumberFormatException e) {
						log.error("ClassCastException pointer exception inside of userLoggedInLoop");
						out.println("Not a number entered");
					} catch (NullPointerException e) {
						log.error("Null pointer exception inside of userLoggedInLoop");
					}
				} catch (NullPointerException e) {
					log.error("Null pointer exception inside of userLoggedInLoop");
				}

			} while (true);
		} else if (input.equalsIgnoreCase("createbankaccount")) {
			List<AccountTypes> list = Arrays.asList(AccountTypes.values());
			do {
				out.println("Please enter the account type or conversely enter \"quit\" to return");
				for (AccountTypes a : list) {
					out.print(a + ",");
				}
				out.println();
				input = in.nextLine();
				if (input.equalsIgnoreCase("quit"))
					break;
				try {
					AccountTypes type = AccountTypes.valueOf(input);
					if (!list.contains(type)) {
						out.println("Invalid Account Type");
						continue;
					}
					out.println("Valid Account Type");
					out.println("Please Enter Balance for the account or conversely enter \"quit\" to return");
					input = in.nextLine();
					if (input.equalsIgnoreCase("quit"))
						continue;
					try {
						double amount = Double.parseDouble(input);
						Account a = new Account(amount, 0L, u, type, false);
						db.create(a);
						out.println("Account Creation Succesful");
						out.println(a);
						break;
					} catch (NumberFormatException e) {
						log.error(e.getMessage());
						out.println("You did not enter a number.");
					}
				} catch (IllegalArgumentException e) {
					log.error(e.getMessage());
				}
			} while (true);
		} else if (input.equalsIgnoreCase("deletebankaccount")) {
			int page = 0;
			do {
				try {
					getUserAccounts(db,page);
					out.println("You have the following accounts:");
					for (int i = 0; i < u.getAccounts().size(); i++) {
						Account account = u.getAccounts().get(i);
						out.println(i + ") " + account);
					}
					out.println("Enter the account index number or conversely enter \"quit\"");
					out.println("You can view more accounts by typing \"nextpage\" \"previouspage\"");
					input = in.nextLine();
					if (input.equalsIgnoreCase("quit"))
						break;
					else if(input.equalsIgnoreCase("previouspage")) {
						page=page==0?0:page-1;
						continue;
					}
					else if (input.equalsIgnoreCase("nextPage")) {
						page++;
						continue;
					}
					try {
						int accountIndex = Integer.parseInt(input);
						Account a = u.getAccounts().get(accountIndex);
						out.println("Are you sure (y/n) or conversely enter \"quit\"");
						input = in.nextLine();
						if (input.equalsIgnoreCase("quit"))
							continue;
						if (input.equalsIgnoreCase("y")) {
							u.getAccounts().remove(a);
							db.delete(a);
						}

					} catch (NumberFormatException e) {
						log.error(e.getMessage());
					}
				} catch (NullPointerException e) {
					log.error(e.getMessage());
				}
			} while (true);
		} else if (input.equalsIgnoreCase("quit")) {
			u = null;
			state = ProgramState.mainLoop;
		}
	}

	public static void getUserAccounts(DatabaseSingleton db,int page) {
		db.getUserAccounts(u,page);
	}

	

	public static void userLoginLoop(PrintStream out, DatabaseSingleton db) {
		log.debug("userLoginLoop");
		out.println("Welcome please press enter or enter quit to return to the previous menu");
		Scanner in = new Scanner(System.in);
		String input = in.nextLine();
		if (input.equalsIgnoreCase("quit")) {
			state = ProgramState.mainLoop;
			return;
		}
		out.println("Please Enter Username");
		String username = in.nextLine();
		out.println("Please Enter Password");
		String password = in.nextLine();
		u = db.getUserByUsernameAndPassword(username, password);
		if (u != null) {
			if (u.isActive()&&!u.isDeleted())
				state = ProgramState.userLoggedInLoop;
			else if (u.isDeleted()){
				out.println("Your account has been deleted.");
			}
			else {
				out.println("Your account has not been activated please contact an administrator.");
			}
		} else
			out.println("Incorrect Login credentials");

	}

}
