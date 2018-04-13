package com.revature.projectzero.accounts;

import java.util.LinkedList;
import java.util.List;

public class User {
	private List<Account> accounts=new LinkedList<Account>();
	private String name=null,lname=null,username=null;
	private boolean isAdmin=false,isActive=true,isDeleted=false;
	private long ID=0L;
	
	public User(String name, String lname, String username, boolean isAdmin, boolean isActive, boolean isDeleted,
			long iD) {
		super();
		this.name = name;
		this.lname = lname;
		this.username = username;
		this.isAdmin = isAdmin;
		this.isActive = isActive;
		this.isDeleted = isDeleted;
		ID = iD;
	}
	public List<Account> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	@Override
	public String toString() {
		return "User [name=" + name + ", lname=" + lname + ", username=" + username + ", isAdmin=" + isAdmin
				+ ", isActive=" + isActive + ", isDeleted=" + isDeleted + ", ID=" + ID + "]";
	}
	
}
