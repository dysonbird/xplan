package com.x.server.loginserver;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

import com.google.protobuf.ByteString;
import com.x.protobuffer.Message.SendMessage;

public class LoginMessage {
	//消息类型
	public final static byte MSG_TYPE_SOCKET =1;//socket
	public final static byte MSG_TYPE_HTTP =2;//http

	private long playerid;	//账号ID
	
	private int sessionid;
	
	private SendMessage sm;
	
	private SendMessage.Builder builder;
	
	private ChannelHandlerContext channelHandlerContent = null;//事件关联的channelContext

	public LoginMessage() {
		builder = SendMessage.newBuilder();
	}
	
	public LoginMessage(SendMessage sm){
		this.setSm(sm);
	}

	public long getPlayerid() {
		return playerid;
	}

	public void setPlayerid(long playerid) {
		this.playerid = playerid;
	}

	public int getSessionid() {
		return sessionid;
	}

	public void setSessionid(int sessionid) {
		this.sessionid = sessionid;
	}

	public SendMessage getSm() {
		return sm;
	}

	public void setSm(SendMessage sm) {
		this.sm = sm;
	}

	public ChannelHandlerContext getChannelHandlerContent() {
		return channelHandlerContent;
	}

	public void setChannelHandlerContent(ChannelHandlerContext channelHandlerContent) {
		this.channelHandlerContent = channelHandlerContent;
	}

	public int getCommand() {
		return sm.getCommand();
	}
	
	public void setCommand(int command){
		builder.setCommand(command);
	}
	
	public byte getMsgType(){
		return sm.getMsgType().byteAt(0);
	}
	
	public void setMsgType(byte type){
		builder.setMsgType(ByteString.copyFrom(new byte[]{type}));
	}
	
	public void putLong(long l){
		builder.addLongValue(l);
	}
	
	public void putByte(byte b){
		builder.addByteValue(ByteString.copyFrom(new byte[]{b}));
	}
	
	public void putBoolean(boolean bool){
		builder.addBoolValue(bool);
	}
	
	public void putInt(int i){
		builder.addIntValue(i);
	}
	
	public void putString(String str){
		builder.addStrValue(str);
	}
	
	public void sendMessage(Channel channel){
		channel.write(builder.build());
	}
}
