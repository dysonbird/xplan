package com.x.manage;

public class DbOper {
	
	public static final short OPER_TYPE_INSERT = 1;
	public static final short OPER_TYPE_UPDATE = 2;
	public static final short OPER_TYPE_DELETE = 3;
	
	public DbOper(short type,Object obj){
		this.operType = type;
		this.operObj = obj;
	}
	
	public short operType = 0;
	public Object operObj = null;
	public int key = 0;

}
