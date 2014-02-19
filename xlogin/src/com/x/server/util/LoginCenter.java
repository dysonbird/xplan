package com.x.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.x.util.Common;

/**
 * @author 
 * Description: 登陆服务器数据中心,多登陆服时只有主服务器生效,即所有登陆服共享一个数据中心
 */
public class LoginCenter {
	private static Logger logger = LoggerFactory.getLogger(LoginCenter.class);
	
	public static int platformType = Common.PLATFORM_TYPE_SELF;//当前平台类型

	//登楼服设定
	private boolean isCreateOpen = true;//帐号注册功能当前是否开放
	private String loginServerIp = "http://192.168.1.???:????";//登录服ip

	public boolean isCreateOpen() {
		return isCreateOpen;
	}

	public void setCreateOpen(boolean isCreateOpen) {
		this.isCreateOpen = isCreateOpen;
	}
	
	public String getLoginServerIp() {
		return loginServerIp;
	}

	public void setLoginServerIp(String loginServerIp) {
		this.loginServerIp = loginServerIp;
	}
}
