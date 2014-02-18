package com.x.server.dbserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.x.server.factory.CustomBeanFactory;

public class XDbServer {
	
	private static Logger logger = LoggerFactory.getLogger(XDbServer.class);

	public static void main(String[] args) {
		XDbServer xdbserver = null;
		try {
			xdbserver = (XDbServer) CustomBeanFactory.getBean("XDbServer");
			xdbserver.init();
			xdbserver.serverStart();
			logger.info("服务器已经已经启动:" + new Date().toString());
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("启动失败:未能获取启动类");
			return;
		}
		
		//服务器main loop
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.println("输入命令:<start/stop>");
			try {
				String str = br.readLine().trim();
				if (str.equals("stop")) {
					xdbserver.serverStop();
					logger.info("服务器已经安全关闭:" + new Date().toString());
					break;
				} else {
					logger.info("不接受此命令: " + str);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("执行命令失败:遇到致命错误");
			}
		}

		System.exit(0);
	}
	
	public void init() {
	}

	public void serverStart() {
		// 启动rmi服务器
//		CustomBeanFactory.getBean("DbProxyServer");
//		CustomBeanFactory.getBean("CommonProxyServer");
	}

	public void serverStop() {
//		AsyPlayerDataTimer.setInterrupted(true);
	}
}
