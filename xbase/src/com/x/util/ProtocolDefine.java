package com.x.util;

public interface ProtocolDefine {
	public final static short FIRST_CONNECT_LOGIN = 1;		//第一次连接登陆服--客户端发来
	
	public final static short LC_PLAYER_CREATE = 2;			//创建用户account
	
	public final static short LC_PLAYER_LOGIN = 3;		//用户account登陆
}
