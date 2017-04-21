/**
 * 第三方用户实体类
 */
package com.bq.core.support.login;

import com.bq.core.base.BaseModel;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Harvey.Wei
 * @version 2016年5月20日 下午3:26:23
 */
@SuppressWarnings("serial")
public class ThirdPartyUser extends BaseModel {

	private String account;// 用户
	private String thirdpartyName;// 第三方用户昵称
	private String userName;// 用户昵称
	private String avatarUrl;// 用户头像地址
	private String gender;// 用户性别
	private String token;// 用户认证
	private String openid;// 用户第三方id
	private String provider;// 用户类型
	private Long userId;// 用户id
	private Boolean verified;//用户类型:(1.普通用户 2.认证用户 3.系统用户)
	private String verifiedReason;//认证原因
	private String description;//描述
	private String location;//用户所在地

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getThirdpartyName() {
		return thirdpartyName;
	}

	public void setThirdpartyName(String thirdpartyName) {
		this.thirdpartyName = thirdpartyName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Boolean getVerified() {
		return verified != null ? verified : false;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
