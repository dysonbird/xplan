package com.x.rmi.login;

import com.x.db.login.Account;

public interface LoginServerRmi {
	public void setCreateOpen(boolean isCreateOpen);	//账号注册功能是否开放
	public boolean isCreateOpen();						//账号注册功能是否开放
	public boolean isOnline(Long accountId);			//账号是否已在线
	public void remove(Long accountId);					//移除主登陆服的登陆账号
	public Long getAccountGameLine(Long accountId);		//账号登陆的游戏服
	//玩家下线
	public void playerLogout(Long accountId, int sessionId, boolean isSet,String actorName, short level, int lineId);
	public int login(Account account);					//账号登陆主登陆服
}
