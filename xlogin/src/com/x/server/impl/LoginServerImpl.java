package com.x.server.impl;

import com.x.rmi.login.LoginServerRmi;
import com.x.server.factory.LoginBeanFactory;
import com.x.server.util.LoginCenter;

public class LoginServerImpl implements LoginServerRmi{
	public static LoginCenter loginCenter = (LoginCenter)LoginBeanFactory.getBean("LoginCenter");

	public LoginCenter getLoginCenter() {
		return loginCenter;
	}

	public void setLoginCenter(LoginCenter loginCenter) {
		LoginServerImpl.loginCenter = loginCenter;
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
