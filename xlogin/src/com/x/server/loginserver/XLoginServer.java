package com.x.server.loginserver;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.x.server.factory.LoginBeanFactory;

public class XLoginServer {
	private static Logger logger = LoggerFactory.getLogger(XLoginServer.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		XLoginServer xloginserver = null;
		try {
			xloginserver = (XLoginServer)LoginBeanFactory.getBean("XLoginServer");
			xloginserver.init();
			xloginserver.serverStart();
			logger.info("服务器已经已经启动:" + new Date().toString());
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("启动失败:未能获取启动类");
			return ;
		}
	}

	public void init(){}
	
	public void serverStart(){}
}
