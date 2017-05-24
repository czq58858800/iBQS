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
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@TableName("bq_user_weibo")
@SuppressWarnings("serial")
public class UserWeibo extends BaseModel {

    /**
     * 微博用户UID
     */
	@TableField("open_id")
	private String openId;
    /**
     * 用户昵称
     */
	@TableField("name_")
	private String name;
    /**
     * 用户个人描述
     */
	@TableField("summary_")
	private String summary;
	@TableField("avatar_")
	private String avatar;
    /**
     * 	是否是微博认证用户，即加V用户，true：是，false：否
     */
	@TableField("verified_")
	private Boolean verified;
    /**
     * 性别，0：女、1：男、2：未知
     */
	@TableField("gender_")
	private String gender;
    /**
     * 用户ID:所属用户的微博好友
     */
	@TableField("user_id")
	private String userId;

	/**
	 * 第三方ID
	 */
	@TableField("thirdparty_id")
	private String thirdpartyId;

	/**
	 * 第三方用户ID
	 */
	@TableField("thirdparty_user_id")
	private String thirdpartyUserId;

    /**
     * true:允许邀请；false：不允许邀请
     */
	@TableField("is_invite")
	private Boolean isInvite;


	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getThirdpartyId() {
		return thirdpartyId;
	}

	public void setThirdpartyId(String thirdpartyId) {
		this.thirdpartyId = thirdpartyId;
	}

	public String getThirdpartyUserId() {
		return thirdpartyUserId;
	}

	public void setThirdpartyUserId(String thirdpartyUserId) {
		this.thirdpartyUserId = thirdpartyUserId;
	}

	public Boolean getIsInvite() {
		return isInvite;
	}

	public void setIsInvite(Boolean isInvite) {
		this.isInvite = isInvite;
	}

}