package com.revature.projectzero.db;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.revature.projectzero.accounts.Account;
import com.revature.projectzero.accounts.AccountTypes;
import com.revature.projectzero.accounts.Transaction;
import com.revature.projectzero.accounts.User;

public class MockDB implements DatabaseSingleton {

	public User getUserByID(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Account getAccountByID(long accounID) {
		// TODO Auto-generated method stub
		return null;
	}

	public void update(User u) {
		// TODO Auto-generated method stub
		
	}

	public void update(Account a) {
		// TODO Auto-generated method stub
		
	}

	public void create(User u,String password) {
		// TODO Auto-generated method stub
		
	}

	public void create(Account a) {
		// TODO Auto-generated method stub
		
	}

	public void delete(User u) {
		// TODO Auto-generated method stub
		
	}

	public void delete(Account a) {
		// TODO Auto-generated method stub
		
	}

	public User getUserByUsernameAndPassword(String username, String password) {
		return new User("Pedro", "Sanchez", "Apo45ty", true, true,false, 0L);
	}

	public boolean isUniqueUsername(String username) {
		// TODO Auto-generated method stub
		if(username.equals("Apo45ty"))
			return true;
		else return false;
	}

	public List<User> getUnActivatedAccounts(int page) {
		List<User> a = new LinkedList<User>( Arrays.asList(
			new User("Pedro", "Gozales", "Apo45ty", true, false, false,0L)
		));
		return a;
	}

	public List<Account> getUserAccounts(User u,int page) {
		List<Account> a = new LinkedList<Account>( Arrays.asList(
				new Account(1000, 0L, u, AccountTypes.savings04092018,false)
		));
		return a;
	}

	@Override
	public List<Transaction> getAccountTransactions(Account a, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Transaction t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void create(Transaction t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Transaction t) {
		// TODO Auto-generated method stub
		
	}
	
}
