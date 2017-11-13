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
@TableName("bq_blacklist")
@SuppressWarnings("serial")
public class Blacklist extends BaseModel {

    /**
     * 用户ID
     */
	@TableField("user_id")
	private String userId;
    /**
     * 加入黑名单用户ID
     */
	@TableField("blacklist_user_id")
	private String blacklistUserId;

	public Blacklist() {

	}
	public Blacklist (String userId,String blacklistUserId) {
		this.userId = userId;
		this.blacklistUserId = blacklistUserId;
	}


    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBlacklistUserId() {
		return blacklistUserId;
	}

	public void setBlacklistUserId(String blacklistUserId) {
		this.blacklistUserId = blacklistUserId;
	}

}