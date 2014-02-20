package com.x.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.google.protobuf.ByteString;
import com.x.factory.XClientPipelineFactory;
import com.x.protobuffer.Message.SendMessage;
import com.x.util.ProtocolDefine;

public class XClient {
	public static int serverPort = 12005;
	private static XClient helloClient = new XClient();

	private static ClientBootstrap bootstrap;

	private static ChannelFuture future;

	public XClient() {
		bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));

		bootstrap.setPipelineFactory(new XClientPipelineFactory());
	}

	public static XClient getInstance() {
		if (helloClient == null) {
			helloClient = new XClient();
		}
		return helloClient;
	}

	public void getChannelFuture(String host, int port) {

		

		if (!future.isSuccess()) {
		}
	}

	public static void main(String[] args) throws InterruptedException,
			IOException {
		String host = "10.6.6.92";
		helloClient = XClient.getInstance();
		future = bootstrap.connect(new InetSocketAddress(host, serverPort));

		while(!future.isSuccess()){
		}
		firstConnect();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.println("输入对话");
			String message = br.readLine().trim();
			if (message.equals("stop")){
				break;
			} else if(message.equals("create")){
				createAccount();
			} else if(message.equals("event")){
				putEvent();
			}
				
		}
		System.exit(0);
	}
	
	private static void firstConnect(){
		SendMessage.Builder builder = SendMessage.newBuilder();
		builder.setCommand(ProtocolDefine.FIRST_CONNECT_LOGIN);
		builder.setMsgType(ByteString.copyFrom(new byte[]{1}));
		builder.addIntValue(3002);
		future.getChannel().write(builder.build());
	}
	
	private static void createAccount() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String message = br.readLine().trim();
		String[] str = message.split("#");
		
		String name = str[0];
		String pwd = str[1];
		String phoneNumber = str[2];
		
		SendMessage.Builder builder = SendMessage.newBuilder();
		builder.setCommand(ProtocolDefine.LC_PLAYER_CREATE);
		builder.setMsgType(ByteString.copyFrom(new byte[]{1}));
		builder.addStrValue(name);
		builder.addStrValue(pwd);
		builder.addStrValue(phoneNumber);
		future.getChannel().write(builder.build());
	}
	
	private static void putEvent(){
//		Message msg = new Message();
//		msg.setCommand(WorldProtocolDefine.CG_LOGIN_ACTORLIST);
//		msg.setString("事件队列测试");
//		future.getChannel().write(msg.buildSendMessage());
	}
}
