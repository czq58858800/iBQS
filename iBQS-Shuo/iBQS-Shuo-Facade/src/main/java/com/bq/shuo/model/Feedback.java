package com.bq.shuo.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bq.shuo.core.base.BaseModel;
import java.io.Serializable;


/**
 * <p>
 * 意见反馈表
 * </p>
 *
 * @author chern.zq
 * @since 2017-04-13
 */
@TableName("bq_feedback")
@SuppressWarnings("serial")
public class Feedback extends BaseModel {

    /**
     * 用户ID
     */
	@TableField("user_id")
	private String userId;
    /**
     * 提交内容
     */
	@TableField("sub_content")
	private String subContent;

	/**
	 * 联系人（联系方式）
	 */
	@TableField("contacts_")
	private String contacts;
    /**
     * 回复内容
     */
	@TableField("reply_content")
	private String replyContent;
    /**
     * 系统用户ID
     */
	@TableField("sys_user_id")
	private String sysUserId;
    /**
     * 回复时间
     */
	@TableField("reply_time")
	private Date replyTime;

	/**
	 * 设备
	 * @return
	 */
	@TableField("device_")
	private String device;

	/**
	 * IP
	 * @return
	 */
	@TableField("ip_")
	private String ip;


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSubContent() {
		return subContent;
	}

	public void setSubContent(String subContent) {
		this.subContent = subContent;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}

	public Date getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}