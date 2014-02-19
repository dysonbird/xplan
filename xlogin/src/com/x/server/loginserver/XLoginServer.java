package com.x.server.loginserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.x.rmi.login.LoginServerRmi;
import com.x.server.factory.LoginBeanFactory;
import com.x.server.util.LoginCenter;
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
		
		//主登陆服务器
		LoginServerRmi mainLoginServer = (LoginServerRmi)LoginBeanFactory.getBean("MainLoginServer");
		
		//登陆服main loop
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); 
		while(true){
			System.out.println("请输入命令:<start/stop/setCreateOpen>"); 
			try {
				String str = br.readLine().trim();
				logger.info("输入命令:" + str);
				if(str.equals("start")){
					System.out.println("服务器正在运行"); 
				}else if(str.equals("stop")){
					xloginserver.serverStop();
					logger.info("服务器已经安全关闭:" + new Date().toString());
					break;
				}else if(str.startsWith("setCreateOpen")){//设置注册功能是否开放:setCreateOpen=true
					if(str.indexOf("true") != -1) {
						mainLoginServer.setCreateOpen(true);
					} else {
						mainLoginServer.setCreateOpen(false);
					}
					logger.info("您已修改isCreateOpen:" + mainLoginServer.isCreateOpen());
				} else {
					System.out.println("不接受此命令: " + str);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("执行命令失败:遇到致命错误");
			}
		}
		
		System.exit(0);
	}

	public void init(){}
	
	public void serverStart(){
		LoginCenter.platformType = platformType;
		
		LoginServer server = LoginServer.getInstance(maxHandleThread, maxFilterThread);
		server.startServer(serverPort);
		
		System.out.println("isMainServer = " + isMainServer);
		if(isMainServer){
			LoginBeanFactory.getBean("LoginServerRmi");//启动供游戏服调用的rmi接口,游戏服通过此接口操作玩家帐号数据
		}
		
//		TextLoader.init(false);//读取预定义字符集
		
		//启动和统一账号系统的连接池
//		ClientFactory.createHttpClientByDefault();
//		LoginBeanFactory.getBean("AccountUtil");
		
		//日志上传线程
//		LogUploadThread logUploadThread = (LogUploadThread)LoginBeanFactory.getBean("LogUploadThread");
//		logUploadThread.setName("文件日志上传线程");
//		logUploadThread.start();
		
//		if(platformType==Common.PLATFORM_TYPE_TENCENT){//腾讯平台
//			logger.info("腾讯平台登录通知接受端启动了");
//			TencentLoginServer tls = TencentLoginServer.getInstance();
//			tls.startServer("", 8001);
//		}
		
		//启动12114短信服务器
//		if(isMainServer && platformType==Common.PLATFORM_TYPE_SELF){//自己平台
//			logger.info("启动12114短信服务器:" + smsIp + ":" + smsPort + smsUrl);
//			SmsHttpServer smsServer = new SmsHttpServer(smsIp,smsPort,smsUrl);
//			smsServer.startServer();
//		}
		
		//启动定时器
//		LoginBeanFactory.getBean("schedulerFactory");
		
//		logger.info("****服务器语言:"+TextLoader.LANG_DEFAULT);
		logger.info("****服务器平台:"+platformType);
	}
	
	public void serverStop(){
		//关闭与统一账号系统的连接池
//		ClientFactory.destoryClient();
	}

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
