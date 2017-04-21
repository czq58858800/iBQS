package com.bq.shuo.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bq.shuo.core.base.BaseModel;
import java.io.Serializable;


/**
 * <p>
 * 会话管理
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@TableName("bq_session")
@SuppressWarnings("serial")
public class Session extends BaseModel {

	@TableField("session_id")
	private String sessionId;
	@TableField("account_")
	private String account;
	@TableField("ip_")
	private String ip;
    /**
     * 设备
     */
	@TableField("device_")
	private String device;
	@TableField("start_time")
	private Date startTime;


	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

}