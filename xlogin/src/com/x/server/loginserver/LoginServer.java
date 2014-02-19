package com.x.server.loginserver;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.x.server.factory.LoginServerPipelineFactory;

public class LoginServer {
	private static Logger logger = LoggerFactory.getLogger(LoginServer.class);
	
	private int maxHandleThread = 10;
	private int maxFilterThread = 10;

	private static LoginServer server = null;
	
	private LoginServer() {
	}

	public static synchronized LoginServer getInstance(
			int maxHandleThread, int maxFilterThread) {
		if (server == null) {
			server = new LoginServer();
			server.maxHandleThread = maxHandleThread;
			server.maxFilterThread = maxFilterThread;
		}
		return server;
	}

public int startServer(int serverPort) {
		
		// Configure the server.
        ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        //create a pipeline factory
        LoginServerPipelineFactory factory = new LoginServerPipelineFactory();//使用了特别工厂类
        
        // Set up the pipeline factory.
        bootstrap.setPipelineFactory(factory);
        
        // 设置相关参数
		bootstrap.setOption("child.tcpNoDelay", true);
		// 设置相关参数
		bootstrap.setOption("child.keepAlive", true);

        // Bind and start to accept incoming connections.
        bootstrap.bind(new InetSocketAddress(serverPort));

		logger.info("登陆服务器socketserver已经启动，端口是 " + serverPort);
	
		
		return 0;
	}

	//停止登陆服socketserver
	public boolean stopServer() {
		return true;
	}
}
