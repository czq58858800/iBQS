package com.bq.shuo.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bq.shuo.core.base.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;


/**
 * <p>
 * 用户表
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@TableName("bq_user")
@SuppressWarnings("serial")
public class User extends BaseModel {

    /**
     * 用户名称
     */
	@TableField("name_")
	private String name;
    /**
     * 登陆帐户
     */
	@TableField("account_")
	private String account;
	@TableField("phone_")
	private String phone;
    /**
     * 登录密码
     */
	@TableField("password_")
	private String password;
    /**
     * 性别(0:女;1:男;2:未知)
     */
	@TableField("sex_")
	private Integer sex;
    /**
     * 头像
     */
	@TableField("avatar_")
	private String avatar;
    /**
     * 简介
     */
	@TableField("summary_")
	private String summary;
	@TableField("address_")
	private String address;
	@TableField("birthday_")
	private String birthday;
    /**
     * 纬度
     */
	@TableField("lat_")
	private Double lat;
    /**
     * 经度
     */
	@TableField("lng_")
	private Double lng;
    /**
     * 用户类型:(1.普通用户 2.微博认证 3.系统认证,4.系统用户)
     */
	@TableField("user_type")
	private String userType;
	@TableField("token_")
	private String token;
	@TableField("login_device")
	private String loginDevice;
	@TableField("push_device_token")
	private String pushDeviceToken;
    /**
     * 推荐用户
     */
	@TableField("rcmd_")
	private Boolean rcmd;
    /**
     * 锁定标志(1:锁定;0:激活)
     */
	@TableField("locked_")
	private Boolean locked;
    /**
     * 最后更新动态时间
     */
	@TableField("last_dynamic_time")
	private Date lastDynamicTime;


	/**
	 * 用户配置
	 */
	@TableField(exist = false)
	private UserConfig config;

	/**
	 * 是否已关注
	 */
	@TableField(exist = false)
	private boolean isFollow;

	/**
	 * 是否允许私信
	 */
	@TableField(exist = false)
	private boolean isMessage;


	/**
	 * 作品数量
	 */
	@TableField(exist = false)
	private int worksNum;

	/**
	 * 作品喜欢总数量
	 */
	@TableField(exist = false)
	private int worksLikeNum;

	/**
	 * 我喜欢的作品数量
	 */
	@TableField(exist = false)
	private int myLikeWorksNum;

	/**
	 * 转发数量
	 */
	@TableField(exist = false)
	private int forwardNum;

	/**
	 * 关注数量
	 */
	@TableField(exist = false)
	private int followNum;

	/**
	 * 粉丝数量
	 */
	@TableField(exist = false)
	private int fansNum;


	public User(){}

	public User(String account,String name,String password,String summary,int sex,String pushDeviceToken,String loginDevice, Double lat,Double lng) {
		this.account = account;
		this.name = name;
		this.password = password;
		this.summary = summary;
		this.sex = sex;
		this.pushDeviceToken = pushDeviceToken;
		this.loginDevice = loginDevice;
		if (lat == null) lat = 0.0;
		if (lng == null) lng = 0.0;
		this.lat = lat;
		this.lng = lng;
		this.locked = false;
		this.userType = "1";
		this.phone = account;
		this.rcmd = false;
	}

	public User(String id, String name, String avatar, String password, Integer sex, String birthday, String address, String summary) {
		super.setId(id);
		this.name = name;
		this.avatar = avatar;
		this.password = password;
		this.sex = sex;
		this.birthday = birthday;
		this.address = address;
		this.summary = summary;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getLoginDevice() {
		return loginDevice;
	}

	public void setLoginDevice(String loginDevice) {
		this.loginDevice = loginDevice;
	}

	public String getPushDeviceToken() {
		return pushDeviceToken;
	}

	public void setPushDeviceToken(String pushDeviceToken) {
		this.pushDeviceToken = pushDeviceToken;
	}

	public Boolean getRcmd() {
		return rcmd;
	}

	public void setRcmd(Boolean rcmd) {
		this.rcmd = rcmd;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public Date getLastDynamicTime() {
		return lastDynamicTime;
	}

	public void setLastDynamicTime(Date lastDynamicTime) {
		this.lastDynamicTime = lastDynamicTime;
	}

	public UserConfig getConfig() {
		return config;
	}

	public void setConfig(UserConfig config) {
		this.config = config;
	}

	public boolean isFollow() {
		return isFollow;
	}

	public void setFollow(boolean follow) {
		isFollow = follow;
	}

	public boolean isMessage() {
		return isMessage;
	}

	public void setMessage(boolean message) {
		isMessage = message;
	}

	public int getWorksNum() {
		return worksNum;
	}

	public void setWorksNum(int worksNum) {
		this.worksNum = worksNum;
	}

	public int getWorksLikeNum() {
		return worksLikeNum;
	}

	public void setWorksLikeNum(int worksLikeNum) {
		this.worksLikeNum = worksLikeNum;
	}

	public int getMyLikeWorksNum() {
		return myLikeWorksNum;
	}

	public void setMyLikeWorksNum(int myLikeWorksNum) {
		this.myLikeWorksNum = myLikeWorksNum;
	}

	public int getForwardNum() {
		return forwardNum;
	}

	public void setForwardNum(int forwardNum) {
		this.forwardNum = forwardNum;
	}

	public int getFollowNum() {
		return followNum;
	}

	public void setFollowNum(int followNum) {
		this.followNum = followNum;
	}

	public int getFansNum() {
		return fansNum;
	}

	public void setFansNum(int fansNum) {
		this.fansNum = fansNum;
	}
}