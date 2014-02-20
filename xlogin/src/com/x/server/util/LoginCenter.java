package com.x.server.util;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.x.db.login.Account;
import com.x.util.Common;

/**
 * 主登陆服数据中心
 * @author 
 * Description: 登陆服务器数据中心,多登陆服时只有主服务器生效,即所有登陆服共享一个数据中心
 */
public class LoginCenter {
	private static Logger logger = LoggerFactory.getLogger(LoginCenter.class);
	
	//所有在线玩家的帐号--多登录服务器下只有主服务器有效
	private static ConcurrentHashMap<Long, Account> allOnlineAccount = new ConcurrentHashMap<Long, Account>();
	
	//玩家登陆临时信息
	private static ConcurrentHashMap<Long, Long> allAccountInfo = new ConcurrentHashMap<Long, Long>();
	
	//所有的游戏服,仅供提取ip用,数据可能不是最新的
	private ConcurrentHashMap<Long, Long> allGameLine = new ConcurrentHashMap<Long, Long>();
	
	public static int platformType = Common.PLATFORM_TYPE_SELF;//当前平台类型

	//登楼服设定
	private boolean isCreateOpen = true;//帐号注册功能当前是否开放
	private String loginServerIp = "http://192.168.1.???:????";//登录服ip

	public boolean isCreateOpen() {
		return isCreateOpen;
	}

	public void setCreateOpen(boolean isCreateOpen) {
		this.isCreateOpen = isCreateOpen;
	}
	
	public String getLoginServerIp() {
		return loginServerIp;
	}

	public void setLoginServerIp(String loginServerIp) {
		this.loginServerIp = loginServerIp;
	}
	
	/**
	 * 
	 * @param playerId
	 * @return
	 */
	public boolean isOnline(long accountId) {
		return allOnlineAccount.containsKey(Long.valueOf(accountId));
	}

	public void remove(Long accountId) {
		allOnlineAccount.remove(accountId);
	}

	/**
	 * 获取账号登陆的游戏服
	 * @param accountId
	 * @return
	 */
	public Long getAccountGameLine(Long accountId) {
		Long lineId = null;
		lineId = allAccountInfo.get(accountId);
		if(lineId != null){
			return allGameLine.get(lineId);
		}
		return lineId;
	}

	/**
	 * 玩家下线
	 * @param accountId
	 * @param sessionId
	 * @param isSet
	 * @param actorName
	 * @param level
	 * @param lineId
	 */
	public void playerLogout(Long accountId, int sessionId, boolean isSet,
			String actorName, short level, int lineId) {
		logger.info("--玩家Logout,accountId:" + accountId + "\t lineId=" + lineId + "\t actorName="+actorName);
		Account account = allOnlineAccount.get(Long.valueOf(accountId));
		if(account != null){
			Long info = allAccountInfo.get(account.getId());
//			if(info == null || info.sessionId != sessionId && sessionId != -1){
//				logger.info("--玩家Logout,sessionId已经过期了,sessionId:" + sessionId);
//				return;
//			}
//			long onlineTime = System.currentTimeMillis() - info.loginTime;//本次在线时长
//			long oldTime = (account.getOnlineTimes() == null? 0 : account.getOnlineTimes());
//			account.setOnlineTimes(oldTime + onlineTime);//更新总在线时长
//			account.setLastLoginTime(new Date());//更新最后登录时间
//			account.updateToDb();//更新帐号信息
//			
//			if(info.lineId != -1){//已经进入分线了
//				int actorSize = gameLineActorCount.get(info.lineId);//本线当前人数
//				if(actorSize > 0){//正常
//					setLineActorCount(Long.valueOf(info.lineId), Integer.valueOf(actorSize - 1));//人数减一
//				}else{//异常,与各线同步
//					setLineActorCount(Long.valueOf(info.lineId), Integer.valueOf(0));//人数=0
////					checkActorCount();//同步各线人数,会卡死,不了~~
//				}
//				info.lineId = -1;
//			}
			
			allOnlineAccount.remove(account.getId());
			allAccountInfo.remove(account.getId());
			
			// 更新角色数据
//			if(isSet){
//				GameLineactor lineactor = getDbProxy().getGameLineactor(account.getId(),lineId);
//				if(lineactor == null){
//					lineactor = new GameLineactor();
//					lineactor.setId(Common.getUUID());
//					lineactor.setAccountId(account.getId());
//					lineactor.setLineId(lineId);
//					lineactor.setActorName(actorName);
//					lineactor.setLevel(level);
//				}
//				getDbProxy().updateOrInsert(lineactor);
//			}
		}else{
			logger.error("##玩家Logout时找不到相应Account,accountId:" + accountId);
		}
		
		//保险操作
		allOnlineAccount.remove(account.getId());
		allAccountInfo.remove(account.getId());
	}

	/**
	 * 账号登陆主登陆服
	 * @param account
	 * @return sessionId
	 */
	public int Login(Account account) {
		allOnlineAccount.put(account.getId(), account);
		Long info = new Long(1);
//		info.loginTime = System.currentTimeMillis();
//		info.sessionId = getSessionId();
		allAccountInfo.put(account.getId(), info);
		return 1;
	}
}
