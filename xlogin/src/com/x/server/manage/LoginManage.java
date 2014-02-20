package com.x.server.manage;

import java.util.Date;
import java.util.HashMap;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.x.db.login.Account;
import com.x.protobuffer.Message.SendMessage;
import com.x.rmi.db.DbProxy;
import com.x.rmi.login.LoginServerRmi;
import com.x.server.factory.LoginBeanFactory;
import com.x.server.loginserver.LoginMessage;
import com.x.server.util.LoginUtil;
import com.x.util.Common;
import com.x.util.MD5;
import com.x.util.ProtocolDefine;

public class LoginManage {
	private static Logger logger = LoggerFactory.getLogger(LoginManage.class);

	// 本登录服所有在线玩家的socket连接对象--多登录服务器下每个服务器只记录连接到自身的玩家
	private HashMap<Long, ChannelHandlerContext> allPlayerCtx = new HashMap<Long, ChannelHandlerContext>();
	
	//主登陆服
	private LoginServerRmi mainLoginServer = null;
	
	//数据库代理
	private DbProxy dbp;
	
	/**
	 * Function name:createPlayerAccount Description:
	 * 创建帐号,返回:0=创建成功,-1=注册功能当前不可用,-2=名称含有敏感字符,
	 * -3=名称长度或格式不符合规定,-4=名称已经被注册了,-5=密码不符合规定,-6=创建失败:原因
	 * @param msg
	 * @param ctx
	 */
	public void createPlayerAccount(LoginMessage msg, ChannelHandlerContext ctx) {
		LoginMessage send = new LoginMessage();
		send.setCommand(msg.getCommand());
		send.setChannelHandlerContent(ctx);
		send.setMsgType(msg.getMsgType());
		boolean isCreateOpen = false;
		try {
			isCreateOpen = this.getMainLoginServer().isCreateOpen();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!isCreateOpen) {// 暂停服务
			send.putByte((byte) -1);// 注册功能当前不可用
			send.putString("注册已关闭");
			send.sendMessage(ctx.getChannel());
			return;
		}

		SendMessage sm = msg.getSm();
		String name = sm.getStrValue(0); 			// 用户名
		String pwd = sm.getStrValue(1); 		    // 密码
		String phoneNumber = sm.getStrValue(2);   // 手机号码

		logger.info("---创建账户 name="+name + "\t pwd="+pwd);
		
		byte rs = LoginUtil.checkName(name, new String[] {Common.PRESET_ACCOUNT_NAME});// 名称检查
		if (rs != 0) {// 名称有误
			send.putByte(rs);
			send.putString("名称有误");
			send.sendMessage(ctx.getChannel());
			return;
		} else {
			Account account;
			try {
				name = name.toLowerCase();// 不区分大小写,统一用小写保存
				account = getDbProxy().getAccountByName(name);
			} catch (Exception e) {
				account = null;
				e.printStackTrace();
			}
			if (account != null) {// 帐号已经存在
				send.putByte((byte) -4);
				send.putString("帐号已经存在");
				send.sendMessage(ctx.getChannel());
				return;
			}
		}

		rs = LoginUtil.checkPassword(pwd);// 密码检查
		if (rs != 0) {
			send.putByte((byte) -5);// 密码有误
			send.putString("密码有误");
			send.sendMessage(ctx.getChannel());
			return;
		}

		try {// 开始创建
			Short channel = (Short) LoginUtil.getParameter(ctx, "channel");// 渠道
			String ipAdd = LoginUtil.getIp(ctx);
			
			Account account = null;
			if(true){
				long accountid = Common.getUUID();
				account = doCreateAccountNew(accountid,name, pwd, phoneNumber,"", channel,0,ipAdd,new Date());
				if (account != null) {// 保存成功
					send.putByte((byte) 0);
				} else {
					// 保存失败,应该是名称重复
					send.putByte((byte) -4);
					send.putString("名称重复");
				}
			}
//			else{
//				send.putByte((byte)-6);
//				send.putString("注册失败");
//			}
			send.sendMessage(ctx.getChannel());
		} catch (Exception e) {
			send.putByte((byte) -6);// 创建失败
			send.putString("注册失败");
			send.sendMessage(ctx.getChannel());
			e.printStackTrace();
		}
		return;
	}
	
	/**
	 * Function name:doCreateAccount
	 * Description: 
	 * @return
	 */
	private Account doCreateAccountNew(long accountid,String name,String pwd,String phoneNumber,String email,
			Short channel,int creatType,String ipAdd,Date createdate){
		
		Account account = new Account();//创建一个帐号
		account.setAccountname(name.toLowerCase());//帐户名,小写保存
		account.setId(accountid);//uid
		account.setPasswd(MD5.toMD5(pwd));//密码
		account.setPhone(phoneNumber);//手机号码
		account.setEmail(email);//邮箱
		account.setCreatedate(createdate);//创建日期
		account.setRegchannel(Integer.valueOf(channel));//客户端渠道号
		account.setCurchannel(Integer.valueOf(channel));//第一次注册登陆的时候 注册渠道等于登陆渠道
		account.setSuperMark((byte)0);//超级账号标志
		boolean isSaveSucc = false;
		try {
			isSaveSucc = getDbProxy().addAccount(account);
		} catch (Exception e) {
//			e.printStackTrace();
			logger.error("##doCreateAccount.getDbProxy().addAccount(account):",e);
		}
		
		return account;
	}
	
	public void firstConnect(LoginMessage msg,ChannelHandlerContext ctx){
		logger.info("客户端第一次连接登陆服 连通性检查");
		SendMessage sm = msg.getSm();
		int channel = sm.getIntValue(0);
		HashMap<String,Object> context = (HashMap<String,Object>)ctx.getAttachment();
		context.put("channel", (short)channel);
		
		SendMessage.Builder builder = SendMessage.newBuilder();
		builder.setCommand(ProtocolDefine.FIRST_CONNECT_LOGIN);
		builder.setMsgType(ByteString.copyFrom(new byte[]{1}));
		builder.addStrValue("登陆服务器返回成功");
		
		ctx.getChannel().write(builder.build());
	}

	public LoginServerRmi getMainLoginServer() {
		return (LoginServerRmi) LoginBeanFactory.getBean("MainLoginServer");
	}
	
	private DbProxy getDbProxy() {
		if (dbp == null) {
			dbp = (DbProxy) LoginBeanFactory.getBean("DbProxy");// 数据库代理
		}
		return dbp;
	}
	
}
