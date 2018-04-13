package com.revature.projectzero.db;

import java.util.List;

import com.revature.projectzero.accounts.Account;
import com.revature.projectzero.accounts.User;

public interface DatabaseSingleton {
	/**
	 * 
	 * @param u
	 * @return
	 */
	List<Account> getUserAccounts(User u,int page);
	/**
	 * 
	 * @param id
	 * @return
	 */
	User getUserByID(long id);

	/**
	 * 
	 * @param accounID
	 * @return
	 */
	Account getAccountByID(long accounID);

	/**
	 * 
	 * @param u
	 */
	void update(User u);

	/**
	 * 
	 * @param a
	 */
	void update(Account a);

	/**
	 * 
	 * @param u
	 */
	void create(User u,String password);

	/**
	 * 
	 * @param a
	 */
	void create(Account a);

	/**
	 * 
	 * @param u
	 */
	void delete(User u);

	/**
	 * 
	 * @param a
	 */
	void delete(Account a);

	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	User getUserByUsernameAndPassword(String username, String password);
	
	/**
	 * 
	 * @param username
	 * @return
	 */
	boolean isUniqueUsername(String username);

	/**
	 * 
	 * @return
	 */
	List<User> getUnActivatedAccounts(int page);
}