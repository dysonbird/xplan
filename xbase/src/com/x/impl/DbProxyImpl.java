package com.x.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.x.db.login.Account;
import com.x.manage.EntityManage;
import com.x.rmi.db.DbProxy;

public class DbProxyImpl implements DbProxy {
	private static Logger logger = LoggerFactory.getLogger(DbProxyImpl.class);
	
	private EntityManage loginEntityManage;
	
	@Override
	public Account getAccountByName(String name) {
		Account account = null;
		try {
			List list = loginEntityManage.findByProperty("Account", "accountname", name);
			if(list != null && list.size() > 0){
				account = (Account)list.get(0);
			}
		} catch (Exception e) {
			logger.info("getAccountByName == null,name = " + name);
		}
		return account;
	}

	@Override
	public boolean addAccount(Account account) {
		try {
			loginEntityManage.save(account);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public EntityManage getLoginEntityManage() {
		return loginEntityManage;
	}

	public void setLoginEntityManage(EntityManage loginEntityManage) {
		this.loginEntityManage = loginEntityManage;
	}
}
