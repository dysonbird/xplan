package com.x.server.util;

import java.util.HashMap;

import org.jboss.netty.channel.ChannelHandlerContext;

import com.x.util.Common;

public class LoginUtil {

	/**
	 * Function name:checkName
	 * Description: 检查帐号名称的合法性
	 * @return 返回:0=正常,-2=名称含有敏感字符,-3=名称长度或格式不符合规定,-7=账号格式有误,请勿使用"gd+数字"进行注册.
	 */
	public static byte checkName(String name){
		if(name == null || name.length()<4 || name.length()>12){//判断长度是否在4到12之间
			return -3;
		}
		
		if(isGuestName(name)){
			return -7;//账号格式有误,请勿使用"gd+数字"进行注册.
		}
		
		if(LoginUtil.isNotNumberOrWord(name)){//名称格式不符合规定
			return -3;
		}else{
			return 0;	
		}
	}
	
	/**
	 * Function name:isGuestName
	 * Description: 是否是试玩账号名
	 * @param name
	 * @return
	 */
	public static boolean isGuestName(String name){
		name = name.toLowerCase();//小写
		if(name.startsWith(Common.HOLE_ACCOUNT_NAME)){//系统预留资源,gd
			String tempStr = name.substring(Common.HOLE_ACCOUNT_NAME.length());
			if(isNumeric(tempStr) == false){
				return false;
			}else{
				return true;
			}
		}else{
			return false;
		}
	}
	
	/**
	 * Function name:checkPassword
	 * Description:检查帐号密码的合法性 
	 * @return 返回:0=正常,1=不符合规定
	 */
	public static byte checkPassword(String pwd){
		if(pwd != null && pwd.length() >= 4 && pwd.length() <= 12){
			if(LoginUtil.isNotNumberOrWord(pwd)){
				return 1;	
			}else{
				return 0;	
			}
		}else{
			return 1;
		}
	}
	
	/**
	 * Function name:isNumeric
	 * Description: 判断字串是否为全数字
	 * @param str：字串
	 * @return：结果 true是 false不是
	 */
	public static boolean isNumeric(String str) {
		if(str == null){
			return false;
		}
		
		for (int i = str.length(); --i >= 0;) {
			int chr = str.charAt(i);
			if (chr < 48 || chr > 57)
				return false;
		}
		return true;
	}
	
	/**
	 * 判断字符是不是字母或数字
	 * @return true:非字母或数字
	 */
	public static boolean isNotNumberOrWord(String str){
		for(int i=0;i<str.length();i++){//判断账号密码里是否都是数字和字母
			if ((str.charAt(i) >= '0' && str.charAt(i) <= '9')
					|| (str.charAt(i) >= 'a' && str.charAt(i) <= 'z')
					|| (str.charAt(i) >= 'A' && str.charAt(i) <= 'Z')) {//是
				continue;
			}else{//否
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param ctx
	 * @return
	 */
	public static String getIp(ChannelHandlerContext ctx) {
		String ipAdd = null;
		try {
			ipAdd = ctx.getChannel().getRemoteAddress().toString();
			// ipAdd=/192.168.1.129:2468
			for (int i = 0; i < ipAdd.length(); i++) {
				char ch = ipAdd.charAt(i);
				if (ch >= '0' && ch <= '9') {// 找到第一个数字
					int end = ipAdd.indexOf(":");
					ipAdd = ipAdd.substring(i, end);
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("不知什么鸟ip=" + ipAdd);
			ipAdd = null;
		}

		return ipAdd;
	}
	
	/**
	 * Function name:getParameter
	 * Description: 取得参数
	 * @param ctx
	 * @param name
	 * @return
	 */
	public static Object getParameter(ChannelHandlerContext ctx,String name){
		try {
			HashMap<String,Object> context = (HashMap<String,Object>)ctx.getAttachment();
			return context.get(name);//渠道
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
