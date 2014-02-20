package com.x.server.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.x.db.login.Account;
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

	/**
	 * 判断账号是否已经在线
	 */
	@Override
	public boolean isOnline(Long accountId) {
		return loginCenter.isOnline(accountId);
	}

	/**
	 * 移除主登陆服的登陆账号
	 */
	@Override
	public void remove(Long accountId) {
		loginCenter.remove(accountId);
	}

	/**
	 * 获取账号登陆的游戏服
	 */
	@Override
	public Long getAccountGameLine(Long accountId) {
		return loginCenter.getAccountGameLine(accountId);
	}

	/**
	 * 玩家下线
	 */
	@Override
	public void playerLogout(Long accountId, int sessionId, boolean isSet,
			String actorName, short level, int lineId) {
		loginCenter.playerLogout(accountId,sessionId,isSet,actorName,level,lineId);
	}

	/**
	 * 账号登陆主登陆服
	 */
	@Override
	public int login(Account account) {
		return loginCenter.Login(account);
	}
}
