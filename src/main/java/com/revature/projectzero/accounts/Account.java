package com.revature.projectzero.accounts;

public class Account {
	private double balance = 0.0d;
	private long id = 0L;
	private User owner;
	private AccountTypes type;
	private boolean isDeleted = false;
	
	
	public Account(double balance, long id, User owner, AccountTypes type, boolean isDeleted) {
		super();
		this.balance = balance;
		this.id = id;
		this.owner = owner;
		this.type = type;
		this.isDeleted = isDeleted;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	public AccountTypes getType() {
		return type;
	}
	public void setType(AccountTypes type) {
		this.type = type;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	@Override
	public String toString() {
		return "Account [balance=" + balance + ", id=" + id + ", owner=" + owner + ", type=" + type + ", isDeleted="
				+ isDeleted + "]";
	}
	
}
