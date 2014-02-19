package com.x.server.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.x.rmi.login.LoginServerRmi;
import com.x.server.util.LoginCenter;

public class LoginServerImpl implements LoginServerRmi{
	
	@Autowired
	public LoginCenter loginCenter;// = (LoginCenter)LoginBeanFactory.getBean("LoginCenter");

	public LoginCenter getLoginCenter() {
		return loginCenter;
	}

	public void setLoginCenter(LoginCenter loginCenter) {
		this.loginCenter = loginCenter;
	}

	/**
	 * @return the isCreateOpen
	 */
	public boolean isCreateOpen() {
		return loginCenter.isCreateOpen();
	}

	/**
	 * @param isCreateOpen the isCreateOpen to set
	 */
	public void setCreateOpen(boolean isCreateOpen) {
		loginCenter.setCreateOpen(isCreateOpen);
	}
}
