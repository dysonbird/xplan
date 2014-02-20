package com.x.rmi.login;

import com.x.db.login.Account;

public interface LoginServerRmi {
	public void setCreateOpen(boolean isCreateOpen);	//�˺�ע�Ṧ���Ƿ񿪷�
	public boolean isCreateOpen();						//�˺�ע�Ṧ���Ƿ񿪷�
	public boolean isOnline(Long accountId);			//�˺��Ƿ�������
	public void remove(Long accountId);					//�Ƴ�����½���ĵ�½�˺�
	public Long getAccountGameLine(Long accountId);		//�˺ŵ�½����Ϸ��
	//�������
	public void playerLogout(Long accountId, int sessionId, boolean isSet,String actorName, short level, int lineId);
	public int login(Account account);					//�˺ŵ�½����½��
}
