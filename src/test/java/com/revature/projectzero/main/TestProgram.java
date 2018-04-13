package com.revature.projectzero.main;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.revature.projectzero.accounts.Account;
import com.revature.projectzero.accounts.User;
import com.revature.projectzero.db.DatabaseSingleton;

public class TestProgram {

	private ByteArrayOutputStream outContent;
	private PrintStream out;
	
	@Before
	public void setUpStreams() {
		outContent = new ByteArrayOutputStream();
		out = new PrintStream(outContent);
		System.out.println("******* START TEST *******");	
	}

	@After
	public void tearDown() throws IOException {
		System.out.println("******* END TEST *******");
		outContent.close();
		out.close();
		System.setIn(System.in);
	}

	@Test
	public void test04() {
		ByteArrayInputStream in = new ByteArrayInputStream((
				"useraccount\r\n" +
				"Apo\r\n" +
				"Apo45ty\r\n" + 
				"Antonio\r\n" +
				"Tapia\r\n" +
				"ATapia\r\n" 
		).getBytes());
		System.setIn(in);
		com.revature.projectzero.main.Program.createAccountLoop(out, new DatabaseSingleton() {
			
			public void update(Account a) {
				// TODO Auto-generated method stub
				
			}
			
			public void update(User u) {
				// TODO Auto-generated method stub
				
			}
			
			public boolean isUniqueUsername(String username) {
				if(username.equalsIgnoreCase("Apo45ty")) {
					return true;
				}
				else 
					return false;
			}
			
			public User getUserByUsernameAndPassword(String username, String password) {
				// TODO Auto-generated method stub
				return null;
			}
			
			public User getUserByID(long id) {
				// TODO Auto-generated method stub
				return null;
			}
			
			public List<Account> getUserAccounts(User u,int page) {
				// TODO Auto-generated method stub
				return null;
			}
			
			public List<User> getUnActivatedAccounts(int page) {
				// TODO Auto-generated method stub
				return null;
			}
			
			public Account getAccountByID(long accounID) {
				// TODO Auto-generated method stub
				return null;
			}
			
			public void delete(Account a) {
				// TODO Auto-generated method stub
				
			}
			
			public void delete(User u) {
				// TODO Auto-generated method stub
				
			}
			
			public void create(Account a) {
				// TODO Auto-generated method stub
				
			}
			
			public void create(User u,String password) {
				// TODO Auto-generated method stub
				
			}
		});;
		System.out.println(outContent.toString());
		String s = "Please enter the type of account you wish to create\r\n" + 
				"enter \"useraccount\" to create a user account\r\n" + 
				"enter \"adminaccount\" to create a admin account\r\n" + 
				"enter \"quit\" to go back to the main menu\r\n" + 
				"\r\n" + 
				"Enter username:\r\n" + 
				"User name taken please pick another!\r\n" + 
				"Enter username:\r\n" + 
				"Username not taken!\r\n" + 
				"Enter Name:\r\n" + 
				"Enter Last Name:\r\n" + 
				"Enter Password:\r\n" + 
				"Account Creation Successful\r\n"
				+ "\r\n" ;
		assertTrue(s.equals(outContent.toString()));
	}
	@Test
	public void test03() {
		ByteArrayInputStream in = new ByteArrayInputStream((
		"quit\r\n").getBytes());
		System.setIn(in);
		com.revature.projectzero.main.Program.mainLoop(out);
		System.out.println(outContent.toString());
		assertTrue(com.revature.projectzero.main.Program.isDone());
	}
	@Test
	public void test02() {
		ByteArrayInputStream in = new ByteArrayInputStream((
		"createaccount\r\n").getBytes());
		System.setIn(in);
		com.revature.projectzero.main.Program.mainLoop(out);
		System.out.println(outContent.toString());
		assertTrue(com.revature.projectzero.main.Program.getState()==ProgramState.createAccountLoop);
	}
	
	@Test
	public void test01() {
		ByteArrayInputStream in = new ByteArrayInputStream((
		"login\r\n").getBytes());
		System.setIn(in);
		com.revature.projectzero.main.Program.mainLoop(out);
		System.out.println(outContent.toString());
		assertTrue(com.revature.projectzero.main.Program.getState()==ProgramState.userLoginLoop);
	}

}
