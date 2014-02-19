package com.x.rmi.db;

import com.x.db.login.Account;

public interface DbProxy {
	public Account getAccountByName(String name);
	
	public boolean addAccount(Account account);
}
