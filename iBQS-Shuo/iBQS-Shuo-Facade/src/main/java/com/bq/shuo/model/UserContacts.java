package com.bq.shuo.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bq.shuo.core.base.BaseModel;
import java.io.Serializable;


/**
 * <p>
 * 
 * </p>
 *
 * @author chern.zq
 * @since 2017-04-13
 */
@TableName("bq_user_contacts")
@SuppressWarnings("serial")
public class UserContacts extends BaseModel {

    /**
     * 设备编号
     */
	@TableField("device_id")
	private String deviceId;
    /**
     * 用户ID
     */
	@TableField("user_id")
	private String userId;
	@TableField("name_")
	private String name;
    /**
     * 手机号
     */
	@TableField("phone_")
	private String phone;

	/**
	 * 联系人用户ID
	 */
	@TableField("contact_user_id")
	private String contactUserId;

	@TableField(exist = false)
	private User user;

	@TableField(exist = false)
	private String status;


	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContactUserId() {
		return contactUserId;
	}

	public void setContactUserId(String contactUserId) {
		this.contactUserId = contactUserId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}