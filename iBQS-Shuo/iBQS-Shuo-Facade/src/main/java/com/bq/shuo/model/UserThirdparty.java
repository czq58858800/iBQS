package com.bq.shuo.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bq.shuo.core.base.BaseModel;
import java.io.Serializable;


/**
 * <p>
 * 第三方用户
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@TableName("bq_user_thirdparty")
@SuppressWarnings("serial")
public class UserThirdparty extends BaseModel {

	@TableField("avatar_")
	private String avatar;
    /**
     * 第三方昵称
     */
	@TableField("name_")
	private String name;
	@TableField("user_id")
	private String userId;
	@TableField("token_")
	private String token;
    /**
     * 第三方类型
     */
	@TableField("provider_")
	private String provider;
    /**
     * 第三方Id
     */
	@TableField("open_id")
	private String openId;
    /**
     * 是否认证
     */
	@TableField("verified_")
	private Boolean verified;
	@TableField("verified_reason")
	private String verifiedReason;

	@TableField(exist = false)
	private User user;

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public String getVerifiedReason() {
		return verifiedReason;
	}

	public void setVerifiedReason(String verifiedReason) {
		this.verifiedReason = verifiedReason;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}