package com.x.server.handler;

import java.util.HashMap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tutorial.Basemessage.BaseMessage;

import com.x.server.factory.LoginBeanFactory;
import com.x.server.loginserver.LoginMessage;
import com.x.server.manage.LoginManage;
import com.x.util.ProtocolDefine;

public class LoginServerHandler extends SimpleChannelUpstreamHandler{
	private static Logger logger = LoggerFactory.getLogger(LoginServerHandler.class);
	public static LoginManage loginManage = (LoginManage)LoginBeanFactory.getBean("LoginManage");
	
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		ChannelBuffer buf = (ChannelBuffer)e.getMessage();
		@SuppressWarnings("unchecked")
		HashMap<String,Object> context = (HashMap<String,Object>)ctx.getAttachment();
		if(context == null){
			System.out.println("--该会话第一次发来消息--");
			context = new HashMap<String,Object>();
			ctx.setAttachment(context);
		}
		
		if(buf.hasArray()){
			byte[] gets = buf.array();
			int msglen = ((gets[0] & 0xFF) << 8) + (gets[1] & 0xFF);
			byte[] msg = gets;
			System.arraycopy(gets, 2, msg, 0, msglen-2);
			BaseMessage bm = (BaseMessage)BaseMessage.parseFrom(msg);
			LoginMessage loginMessage = new LoginMessage(bm);
			System.out.println("命令码: " + loginMessage.getCommand());
			int len = 0;
			len = msglen;
			byte[] send = new byte[len];
			send[0] = (byte)((msglen & 0xFF) >> 8);
			send[1] = (byte)(msglen & 0xFF);
			System.arraycopy(gets, 0, send, 2, msglen-2);
			ChannelBuffer cb = ChannelBuffers.buffer(send.length);
			cb.writeBytes(send);
			ctx.getChannel().write(cb);
		}
		
//		try {
//			if(bm != null){
////				byte[] gets = msg.getBytes();
////				System.out.println("Server Byte[]: " + Arrays.toString(gets));
////				BaseMessage sm = (BaseMessage)BaseMessage.parseFrom(msg);
////				LoginMessage loginMessage = new LoginMessage(bm);
//				
//				loginMessage.setPlayerid(context.get("accountId")==null?0:((Long)context.get("accountId")));
//				loginMessage.setChannelHandlerContent(ctx);
//				System.out.println("命令码: " + loginMessage.getCommand() + " MsgType: " + loginMessage.getMsgType());
////				ctx.getChannel().write(bm);
//				//handleMessage(loginMessage,ctx);
//			}
//		} catch(Exception ex){
//			ex.printStackTrace();
//		}
		
	}
	
	/**
	 * Function name:handleMessage
	 * Description: 消息处理
	 * @param msg
	 * @param ctx
	 */
	public void handleMessage(LoginMessage msg,ChannelHandlerContext ctx){
		logger.info("---command="+msg.getCommand());
		switch (msg.getCommand()) {
		case ProtocolDefine.FIRST_CONNECT_LOGIN://第一次连接 连通性检查
			loginManage.firstConnect(msg, ctx);
			break;
		case ProtocolDefine.LC_PLAYER_CREATE://注册账号
			loginManage.createPlayerAccount(msg, ctx);
			break;
		case ProtocolDefine.LC_PLAYER_LOGIN://用户account登陆
			loginManage.login(msg, ctx);
			break;
		default:
			logger.info("--未知消息--" + msg.getCommand());
			break;
		}
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		// 客户端连接成功
		System.out.println("--客户端连接成功--");
		super.channelConnected(ctx, e);
	}
	
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		//客户端断开连接
		super.channelClosed(ctx, e);
		HashMap<String,Object> context = (HashMap<String,Object>)ctx.getAttachment();
		if(context != null){
			Long accountId = (Long)context.get("accountId");
			if(accountId == null){//还未登陆
				//logger.info("--玩家下线:还未登陆");
			}else{
				//登陆成功后 会断开登陆服的链接 玩家链接到游戏服
				loginManage.removeAccountCtx(accountId);
				logger.info("玩家:" + accountId +" 跳转到游戏服,断开登陆服的连接");
			}
		}else{
			logger.info("--玩家断开连接:未产生任何操作,ip:"+ctx.getChannel().getRemoteAddress().toString());
		}
	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		ctx.getChannel().close();
		e.getChannel().close();
	}
}
