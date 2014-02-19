package com.x.util;

import java.util.UUID;

public class Common {
	public static long LONG_MAX_VALUE=(long)Math.pow(2, 63);	
	public final static int PLATFORM_TYPE_SELF = 1;
	
	public static final String HOLE_ACCOUNT_NAME = "x";//保留的帐号名称,做快速注册用

	/**
	 * 
	 * Function name:getUUID
	 * Description: 获取全球唯一编号的方法
	 * @return：唯一编号
	 */
	public static long getUUID(){
		UUID uuid = UUID.randomUUID(); 
		return uuid.getLeastSignificantBits()+LONG_MAX_VALUE;
	}
}
