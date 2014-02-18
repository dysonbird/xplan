package com.x.server.loginserver;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.x.server.factory.LoginBeanFactory;
import com.x.util.Common;

public class XLoginServer {
	private static Logger logger = LoggerFactory.getLogger(XLoginServer.class);

	private boolean isMainServer = true;//是否是主登陆服务器,是的话要启动数据中心
	private int serverPort = 20000;//供客户端连接的端口
	private int maxHandleThread = 10;
	private int maxFilterThread = 10; 
	
	private int serverPortHttp = 8079;//供客户端连接的端口
	
	private int platformType = Common.PLATFORM_TYPE_SELF;//平台类型
	
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

	public boolean isMainServer() {
		return isMainServer;
	}

	public void setMainServer(boolean isMainServer) {
		this.isMainServer = isMainServer;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public int getMaxHandleThread() {
		return maxHandleThread;
	}

	public void setMaxHandleThread(int maxHandleThread) {
		this.maxHandleThread = maxHandleThread;
	}

	public int getMaxFilterThread() {
		return maxFilterThread;
	}

	public void setMaxFilterThread(int maxFilterThread) {
		this.maxFilterThread = maxFilterThread;
	}

	public int getServerPortHttp() {
		return serverPortHttp;
	}

	public void setServerPortHttp(int serverPortHttp) {
		this.serverPortHttp = serverPortHttp;
	}

	public int getPlatformType() {
		return platformType;
	}

	public void setPlatformType(int platformType) {
		this.platformType = platformType;
	}
	
}
