package com.x.server.loginserver;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

import tutorial.Basemessage.BaseMessage;

import com.google.protobuf.ByteString;

public class LoginMessage {
	//消息类型
	public final static byte MSG_TYPE_SOCKET =1;//socket
	public final static byte MSG_TYPE_HTTP =2;//http

	private long playerid;	//账号ID
	
	private int sessionid;
	
	private BaseMessage bm;
	
	private BaseMessage.Builder builder;
	
	private ChannelHandlerContext channelHandlerContent = null;//事件关联的channelContext

	public LoginMessage() {
		builder = BaseMessage.newBuilder();
	}
	
	public LoginMessage(BaseMessage bm){
		this.setBm(bm);
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

	public BaseMessage getBm() {
		return bm;
	}

	public void setBm(BaseMessage bm) {
		this.bm = bm;
	}

	public ChannelHandlerContext getChannelHandlerContent() {
		return channelHandlerContent;
	}

	public void setChannelHandlerContent(ChannelHandlerContext channelHandlerContent) {
		this.channelHandlerContent = channelHandlerContent;
	}

	public int getCommand() {
		return bm.getCommand();
	}
	
	public void setCommand(int command){
		builder.setCommand(command);
	}
	
	public byte getMsgType(){
		return bm.getMsgType().byteAt(0);
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
