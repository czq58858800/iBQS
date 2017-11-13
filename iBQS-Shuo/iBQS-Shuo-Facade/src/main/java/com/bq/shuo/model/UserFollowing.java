package com.bq.shuo.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bq.shuo.core.base.BaseModel;
import java.io.Serializable;


/**
 * <p>
 * 用户关注表
 * </p>
 *
 * @author chern.zq
 * @since 2017-04-13
 */
@TableName("bq_user_following")
@SuppressWarnings("serial")
public class UserFollowing extends BaseModel {

    /**
     * 关注者用户ID
     */
	@TableField("follow_user_id")
	private String followUserId;
    /**
     * 被关注者用户ID
     */
	@TableField("befollow_user_id")
	private String befollowUserId;

	public UserFollowing(){}

	public UserFollowing(String followUserId,String befollowUserId) {
		this.followUserId = followUserId;
		this.befollowUserId = befollowUserId;
	}


	public String getFollowUserId() {
		return followUserId;
	}

	public void setFollowUserId(String followUserId) {
		this.followUserId = followUserId;
	}

	public String getBefollowUserId() {
		return befollowUserId;
	}

	public void setBefollowUserId(String befollowUserId) {
		this.befollowUserId = befollowUserId;
	}

}