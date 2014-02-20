package com.x.handler;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.x.protobuffer.Message.SendMessage;
import com.x.util.ProtocolDefine;

public class XClinetHandler extends SimpleChannelUpstreamHandler{
	
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		SendMessage sm = (SendMessage)e.getMessage();
		System.out.println("√¸¡Ó¬Î: " + sm.getCommand());
		switch(sm.getCommand()){
		case ProtocolDefine.FIRST_CONNECT_LOGIN:
			System.out.println(sm.getStrValue(0));
			break;
		case ProtocolDefine.LC_PLAYER_CREATE:
			byte rs = sm.getByteValue(0).byteAt(0);
			if(rs < 0){
				System.out.println(sm.getStrValue(0));
			} else{
				System.out.println("◊¢≤·≥…π¶");
			}
			break;
		case ProtocolDefine.LC_PLAYER_LOGIN:
			if(sm.getByteValue(0).byteAt(0) < 0){
				System.out.println(sm.getStrValue(0));
			} else{
				System.out.println("’À∫≈ID£∫" + sm.getLongValue(0));
				System.out.println("sessionID£∫" + sm.getIntValue(0));
			}
			break;
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
	throws Exception {

		super.exceptionCaught(ctx, e);

	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		System.out.println("connect server success");
	}
	
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		System.out.println("server closed");
	}
}
