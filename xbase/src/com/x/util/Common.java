package com.x.util;

import java.util.UUID;

public class Common {
	public static long LONG_MAX_VALUE=(long)Math.pow(2, 63);	
	public final static int PLATFORM_TYPE_SELF = 1;
	
	public static final String HOLE_ACCOUNT_NAME = "x";//�������ʺ�����,������ע����

	/**
	 * 
	 * Function name:getUUID
	 * Description: ��ȡȫ��Ψһ��ŵķ���
	 * @return��Ψһ���
	 */
	public static long getUUID(){
		UUID uuid = UUID.randomUUID(); 
		return uuid.getLeastSignificantBits()+LONG_MAX_VALUE;
	}
}
