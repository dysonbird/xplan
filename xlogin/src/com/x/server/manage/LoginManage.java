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
import com.x.server.util.LoginCenter;
import com.x.server.util.LoginUtil;
import com.x.util.Common;
import com.x.util.MD5;
import com.x.util.ProtocolDefine;

public class LoginManage {
	private static Logger logger = LoggerFactory.getLogger(LoginManage.class);

	// 本登录服所有在线玩家的socket连接对象--多登录服务器下每个服务器只记录连接到自身的玩家
	private HashMap<Long, ChannelHandlerContext> allAccountCtx = new HashMap<Long, ChannelHandlerContext>();
	
	//主登陆服
	private static LoginServerRmi mainLoginServer = null;
	
	//数据库代理
	private DbProxy dbp;
	
	/**
	 * 账号登陆
	 * @param msg
	 * @param ctx
	 */
	public void login(LoginMessage msg, ChannelHandlerContext ctx){
		int channel = (Short) LoginUtil.getParameter(ctx, "channel") == null ? 0: (Short) LoginUtil.getParameter(ctx, "channel");// 渠道
		logger.info("登录玩家的渠道号=" + channel+" 平台号="+LoginCenter.platformType);
		
		//判断channel登陆到特定的平台
		
		//官服渠道登陆
		Account account = loginForSelf(msg, ctx, channel);
	}
	
	/**
	 * 
	 * @param msg
	 * @param ctx
	 * @param channel 登陆渠道
	 * @return
	 */
	private Account loginForSelf(LoginMessage msg, ChannelHandlerContext ctx, int channel){
		Account account = null;
		byte rs = 0;	//登陆结果 小于0 登陆失败
		
		SendMessage sm = msg.getSm();
		String name = sm.getStrValue(0); 		// 用户名
		String pwd = sm.getStrValue(1); 		// 密码
		String handset = sm.getStrValue(2);	// 平台 iphone or android
		String phoneModel = sm.getStrValue(3); // 手机型号
		String machineNo = sm.getStrValue(4); // 设备唯一标识
		
		byte connMode = msg.getMsgType();	// 连接方式,1是socket 2是http

		LoginMessage send = new LoginMessage();
		send.setCommand(msg.getCommand());
		send.setChannelHandlerContent(ctx);
		send.setMsgType(msg.getMsgType());
		
		String ipAdd = LoginUtil.getIp(ctx);
		try {
			name = name.toLowerCase();// 不区分大小写,统一用小写保存
			logger.info("--玩家Login,name = " + name + ",ip = " + ipAdd + ",netType = " + connMode);

			if (!isMobileIp(ipAdd)) {// 非法ip:您的Ip地址xxx无法登陆游戏,如有疑问请联系客服.
				logger.info("非法ip无法登陆 = " + ipAdd);
				rs = -1;	//非法IP 不能登录
			} else if (isPwdErrorToMany(name)) {
				rs = -2;
				logger.info("多次输入错误密码,name = " + name + ",ip = " + ipAdd);
			} else {
				account = getDbProxy().getAccountByName(name);
				if(account==null){
					//去统一账号系统找
					rs = -3;	//账号不存在
				}
				
				if(rs == 0){
					if (account.getLocktime() != null
							&& account.getLocktime() < System.currentTimeMillis()) {// 处于被锁定状态,检查是否可以解锁了
						account.setStatus(Common.ACCOUNT_STATUS_NORMAL);
						account.setLocktime(0L);// 解封
						account.setLockwhys("");
					}

					try {
						account.setLastLoginTime(new Date());// 更新最后登录时间
						account.setTerminalinfo(handset);// 平台
						
						if ( account.getMachineNo()==null || account.getMachineNo().length()==0) { // 设备唯一信息
							account.setMachineNo(machineNo);
						}
						account.setPhoneModel(phoneModel);        // 机型信息
						account.setCurchannel(Integer.valueOf(channel));
						
						getDbProxy().updateAccount(account);// 更新帐号信息
					} catch (Exception e2) {
						logger.error("##accountLogin:更新账号状态出错.", e2);
					}

					String pwdMd5 = (pwd == null ? null : MD5.toMD5(pwd));
					if (pwd == null || "".equals(pwd.trim())) {
						rs = -4;	//密码为空
					} else if (!pwd.equals(account.getPasswd())
							&& !pwdMd5.equals(account.getPasswd())) {
						rs = -5;	//密码错误
						errorPassword(name);//记录密码错误
					} else if (account.getStatus() != null
							&& (account.getStatus() == Common.ACCOUNT_STATUS_LOCK 
							|| account.getStatus() == Common.ACCOUNT_STATUS_LOCK_SELF)) {
						rs = -6;	//账号锁定
					} else {
						//可以正常登录
						removeErrorPassword(name);

						//判断账号是否已经在线
						boolean isOl = false;
						try {
							isOl = this.getMainLoginServer().isOnline(account.getId());
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						if (isOl) {
							// 玩家已经在线在,先T
							forceOffline(account.getId());
						}

						// 开始登录
						int sessionId = doLogin(account, ctx, msg.getMsgType());

						send.putByte((byte) 0);				// 返回登陆成功
						send.putLong(account.getId());		// 返回玩家对应的帐号id
						send.putInt(sessionId);				// sessionId

						// 日志
					}
					
				}
			}
		} catch (Exception e) {
			rs = -7;
			logger.error("登陆异常: ", e);
		}

		if (rs < 0) {// 异常的处理
			send.putByte(rs);
//			String str = TextLoader.getErrorText((Byte) LoginUtil.getParameter(
//					ctx, "lang"), ProtocolDefine.LC_PLAYER_LOGIN, rs);
//			if (rs == LoginDefine.BACK_ERROR_LOGIN_ACCOUNT_LOCK) {// 加上锁定原因
//				str += ("<" + account.getLockwhys() + ">.");
//			} else if (rs == LoginDefine.BACK_ERROR_LOGIN_IP_ILLEGAL) {
//				// 您的Ip地址xxx无法登陆游戏,如有疑问请联系客服.
//				str = str.replaceFirst("xxx", ipAdd);
//			}
			send.putString("登陆失败");
		}
		send.sendMessage(ctx.getChannel());
		return account;
	}
	
	private int doLogin(Account account, ChannelHandlerContext ctx, byte msgType) {
		HashMap<String, Object> context = (HashMap<String, Object>) ctx.getAttachment();
		context.put("accountId", account.getId());// 记录连接对应的帐号id
		context.put("account", account);
		int sessionId = 0;
		try {
			sessionId = this.getMainLoginServer().login(account);// 通知主登陆服务器
		} catch (Exception e) {
			e.printStackTrace();
		}

//		if (msgType == LoginMessage.MSG_TYPE_SOCKET) {// http无需保存
//			allPlayerCtx.put(Long.valueOf(account.getId()), ctx);// 本地保存socket
//		}

//		if (allPlayerCtx.size() > 30000) {
//			logger.info("##allPlayerCtx数量很大:" + allPlayerCtx.size());
//		}
		
		return sessionId;
	}

	/**
	 * 强制玩家下线 T人
	 * @param id
	 */
	private void forceOffline(Long accountId) {
		ChannelHandlerContext oldCtx = allAccountCtx.get(Long.valueOf(accountId));
		if (oldCtx != null) {
			//在本登陆服,且没有跳到游戏服 如果已经成功登陆但未进入游戏服  数据会在主登陆服数据中心
			oldCtx.getChannel().close();// 强行关闭旧连接
			logger.info("--玩家强制登录,同登陆服,playerId:" + accountId);
		} else {
			//在其他登陆服或者已经跳转到游戏服
			Long gameSubline = null;	//账号登陆到的游戏服ID
			try {
				gameSubline = this.getMainLoginServer().getAccountGameLine(accountId);	//去主登陆服获取账号登陆的游戏服
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if (gameSubline != null) {
				//通知具体的游戏服T角色下线
//				try {
//					LogicServer logicServer = LoginUtil.getLogicServer(gamesubline.getLocalIp(),gamesubline.getLineport() + 1);
//					if (logicServer != null) {
//						logicServer.removeActor(accountId, -1);//通知游戏服T人
//						playerLogout(accountId, -1,false,"",(short)0,0);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
			} else {
				logger.info("--玩家强制登录,不同登陆服且未找到玩家所在的游戏服,accountId:" + accountId);
				//没有找到账号在那个登陆服 保险操作也做一下登楼服的账号下线
				playerLogout(accountId, -1,false,"",(short)0,0);
			}
		}
	}

	/**
	 * 登陆服玩家下线
	 * @param accountId
	 * @param sessionId
	 * @param isSet
	 * @param actorName
	 * @param level
	 * @param lineId
	 */
	private void playerLogout(Long accountId, int sessionId, boolean isSet, String actorName,
			short level, int lineId) {
		try {
			this.getMainLoginServer().playerLogout(accountId, sessionId, isSet, actorName,level, lineId);
		} catch (Exception e) {
			e.printStackTrace();
		}

//		if (allPlayerCtx.remove(Long.valueOf(playerId)) == null) {
//			logger.error("##玩家Logout时找不到相应ctx,playerId:" + playerId);
//		}
	}

	/**
	 * 清除密码错误记录
	 * @param name
	 */
	private void removeErrorPassword(String name) {
	}

	/**
	 * 记录账号密码错误的时间 次数
	 * @param name
	 */
	private void errorPassword(String name) {
	}

	/**
	 * 密码错误过多 一段时间后可以登录
	 * @param name
	 * @return
	 */
	private boolean isPwdErrorToMany(String name) {
		return false;
	}

	/**
	 * 判断账号的登陆IP是否合法
	 * @param ipAdd
	 * @return
	 */
	private boolean isMobileIp(String ipAdd) {
		return true;
	}

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
		try {
			getDbProxy().addAccount(account);
		} catch (Exception e) {
			logger.error("##doCreateAccount.getDbProxy().addAccount(account):", e);
		}
		
		return account;
	}
	
	public void firstConnect(LoginMessage msg,ChannelHandlerContext ctx){
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
		if(mainLoginServer == null){
			mainLoginServer = (LoginServerRmi) LoginBeanFactory.getBean("MainLoginServer");
		}
		return mainLoginServer;
	}
	
	private DbProxy getDbProxy() {
		if (dbp == null) {
			dbp = (DbProxy) LoginBeanFactory.getBean("DbProxy");// 数据库代理
		}
		return dbp;
	}

	/**
	 * 移除玩家账号ID 玩家链接到游戏服
	 * @param accountId
	 */
	public void removeAccountCtx(Long accountId) {
		// 移除本登陆服的数据 数据已经在主登陆服
		allAccountCtx.remove(Long.valueOf(accountId));
		
		//移除主登陆服的数据？
//		mainLoginServer.remove(accountId);
	}
	
}
