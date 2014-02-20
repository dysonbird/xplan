package com.x.db.login;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Account entity. 
 */
@Entity
@Table(name="tb_account")
public class Account  implements java.io.Serializable {

	// Fields
	/**
	 * 
	 */
	private static final long serialVersionUID = 145296938216105330L;
	
	@Id
	@Column(columnDefinition = " bigint(20) NOT NULL COMMENT '账号编号:唯一标识' ")
	private Long id;
	
	@Column(columnDefinition = " varchar(100) DEFAULT NULL COMMENT '账号名'")
	private String accountname;
	
	@Column(columnDefinition = " char(64) DEFAULT NULL COMMENT '账号密码:md5加密' ")
	private String passwd;
	
	@Column(columnDefinition = " varchar(1024) DEFAULT NULL COMMENT '账号密码扩展信息:用于密码寻回，由多个数据组合而成'")
	private String passwdexpand;
	
	@Column(columnDefinition = " int(11) DEFAULT NULL COMMENT '货币值:无用，保留'")
	private Integer currency;
	
	@Column(columnDefinition = " datetime DEFAULT NULL COMMENT '创建时间'")
	private Date createdate;
	
	@Column(columnDefinition = " int(11) DEFAULT NULL COMMENT '注册途径:client、wap、web等，暂时未使用'")
	private Integer regway;
	
	@Column(columnDefinition = " int(11) DEFAULT NULL COMMENT '注册渠道:重要，用于标识用户来自哪个渠道'")
	private Integer regchannel;
	
	@Column(columnDefinition = "char(16) DEFAULT NULL COMMENT '用户手机号码'")
	private String phone;
	
	@Column(columnDefinition = "varchar(255) DEFAULT NULL COMMENT '手机型号'")
	private String phoneModel;
	
	@Column(columnDefinition = "varchar(255) DEFAULT NULL COMMENT '设备唯一标识'")
	private String machineNo;
	
	
	@Column(columnDefinition = "char(64) DEFAULT NULL COMMENT '邮箱地址'")
	private String email;
	
	@Column(columnDefinition = "varchar(256) DEFAULT NULL COMMENT '终端信息'")
	private String terminalinfo;
	
	@Column(columnDefinition = "  varchar(1024) DEFAULT NULL COMMENT '个人信息' ")
	private String personalinfo;
	
	@Column(columnDefinition ="bigint(20) DEFAULT NULL COMMENT '总在线时长'")
	private Long onlineTimes;	  //在线总时长
	
	@Column(columnDefinition = " datetime DEFAULT NULL COMMENT '最后一次登录时间' ")
	private Date lastLoginTime;   //最后一次登录时间
	
	@Column(columnDefinition = " tinyint(4) DEFAULT NULL COMMENT '当前状态'")
	private Byte status;		  //帐号当前状态
	
	@Column(columnDefinition = "datetime DEFAULT NULL COMMENT '最后一次状态改变时间'")
	private Date lastStatusChange;//最后一次状态改变时间
	
	@Column(columnDefinition = " varchar(1024) DEFAULT NULL COMMENT '各分区角色数记录'")
	private String actorSize;	  //在各分区注册的角色数量,格式:分区id-角色数量|分区id-角色数量
	
	@Column(columnDefinition = "bigint(20) DEFAULT NULL COMMENT '账号锁定时间'")
	private Long locktime;		  //>0表示被封号了
	
	@Column(columnDefinition = "varchar(30) DEFAULT NULL COMMENT '账号锁定原因'")
	private String lockwhys;	  //被封号的原因
	
	@Column(columnDefinition="tinyint(4) DEFAULT '0' COMMENT '超级账号标识:0非超级账号 1超级账号'")
	private Byte superMark;
	
	@Column(columnDefinition="int(11) DEFAULT NULL COMMENT '登录渠道'")
	private Integer curchannel;  //玩家当前登录渠道
	

	// Constructors

	/** default constructor */
	public Account() {
	}

	/** minimal constructor */
	public Account(Long id) {
		this.id = id;
	}

	/** full constructor */
	public Account(Long id, String accountname, String passwd,
			String passwdexpand, Integer currency, Date createdate,
			Integer regway, Integer regchannel, String phone, String email,
			String terminalinfo, Short resourcelevel, String interactionkey,
			String activatedArea,Byte superMark
			) {
		this.id = id;
		this.accountname = accountname;
		this.passwd = passwd;
		this.passwdexpand = passwdexpand;
		this.currency = currency;
		this.createdate = createdate;
		this.regway = regway;
		this.regchannel = regchannel;
		this.phone = phone;
		this.email = email;
		this.terminalinfo = terminalinfo;
		this.superMark = superMark;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountname() {
		return this.accountname;
	}

	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}

	public String getPasswd() {
		return this.passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getPasswdexpand() {
		return this.passwdexpand;
	}

	public void setPasswdexpand(String passwdexpand) {
		this.passwdexpand = passwdexpand;
	}

	public Integer getCurrency() {
		return this.currency;
	}

	public void setCurrency(Integer currency) {
		this.currency = currency;
	}

	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Integer getRegway() {
		return this.regway;
	}

	public void setRegway(Integer regway) {
		this.regway = regway;
	}

	public Integer getRegchannel() {
		return this.regchannel;
	}

	public void setRegchannel(Integer regchannel) {
		this.regchannel = regchannel;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTerminalinfo() {
		return this.terminalinfo;
	}

	public void setTerminalinfo(String terminalinfo) {
		this.terminalinfo = terminalinfo;
	}
	
	public Byte getSuperMark() {
		return superMark;
	}

	public void setSuperMark(Byte superMark) {
		this.superMark = superMark;
	}

	public void setCurchannel(Integer curchannel) {
		this.curchannel = curchannel;
	}

	public Integer getCurchannel() {
		return curchannel;
	}

	/**
	 * @return the personalinfo
	 */
	public String getPersonalinfo() {
		return personalinfo;
	}

	/**
	 * @param personalinfo the personalinfo to set
	 */
	public void setPersonalinfo(String personalinfo) {
		this.personalinfo = personalinfo;
	}

	/**
	 * @return the onlineTimes
	 */
	public Long getOnlineTimes() {
		return onlineTimes;
	}

	/**
	 * @param onlineTimes the onlineTimes to set
	 */
	public void setOnlineTimes(Long onlineTimes) {
		this.onlineTimes = onlineTimes;
	}

	/**
	 * @return the lastLoginTime
	 */
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	/**
	 * @param lastLoginTime the lastLoginTime to set
	 */
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	/**
	 * @return the status
	 */
	public Byte getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Byte status) {
		this.status = status;
	}

	/**
	 * @return the lastStatusChange
	 */
	public Date getLastStatusChange() {
		return lastStatusChange;
	}

	/**
	 * @param lastStatusChange the lastStatusChange to set
	 */
	public void setLastStatusChange(Date lastStatusChange) {
		this.lastStatusChange = lastStatusChange;
	}

	/**
	 * @return the actorSize
	 */
	public String getActorSize() {
		return actorSize;
	}

	/**
	 * @param actorSize the actorSize to set
	 */
	public void setActorSize(String actorSize) {
		this.actorSize = actorSize;
	}

	/**
	 * @return the locktime
	 */
	public Long getLocktime() {
		return locktime;
	}

	/**
	 * @param locktime the locktime to set
	 */
	public void setLocktime(Long locktime) {
		this.locktime = locktime;
	}

	/**
	 * @return the lockwhys
	 */
	public String getLockwhys() {
		return lockwhys;
	}

	/**
	 * @param lockwhys the lockwhys to set
	 */
	public void setLockwhys(String lockwhys) {
		this.lockwhys = lockwhys;
	}

	/**
	 * @return the phoneModel
	 */
	public String getPhoneModel() {
		return phoneModel;
	}

	/**
	 * @param phoneModel the phoneModel to set
	 */
	public void setPhoneModel(String phoneModel) {
		this.phoneModel = phoneModel;
	}

	/**
	 * @return the machineNo
	 */
	public String getMachineNo() {
		return machineNo;
	}

	/**
	 * @param machineNo the machineNo to set
	 */
	public void setMachineNo(String machineNo) {
		this.machineNo = machineNo;
	}

}