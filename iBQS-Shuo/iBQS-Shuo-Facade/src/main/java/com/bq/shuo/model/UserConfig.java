package com.bq.shuo.model;

import com.baomidou.mybatisplus.annotations.TableId;
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
@TableName("bq_user_config")
@SuppressWarnings("serial")
public class UserConfig extends BaseModel {

    /**
     * 用户ID
     */
	private String userId;
    /**
     * 是否允许评论
     */
	@TableField("is_comment")
	private Boolean isComment;
    /**
     * 是否开启 只允许关注的用户评论
     */
	@TableField("is_comment_follow")
	private Boolean isCommentFollow;
    /**
     * 是否允许私信
     */
	@TableField("is_message")
	private Boolean isMessage;
    /**
     * 是否开启 只允许关注的用户私信
     */
	@TableField("is_message_follow")
	private Boolean isMessageFollow;
    /**
     * 是否允许其他用户修改我的作品
     */
	@TableField("is_works")
	private Boolean isWorks;
    /**
     * 是否开启 只允许关注的用户改图
     */
	@TableField("is_works_follow")
	private Boolean isWorksFollow;

	public UserConfig() {

	}

	public UserConfig(String userId,boolean isMessage, boolean isWorks, boolean isComment, boolean isMessageFollow, boolean isWorksFollow, boolean isCommentFollow) {
		this.userId = userId;
		this.isMessage = isMessage;
		this.isWorks = isWorks;
		this.isComment = isComment;
		this.isMessageFollow = isMessageFollow;
		this.isWorksFollow = isWorksFollow;
		this.isCommentFollow = isCommentFollow;
	}


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Boolean getIsComment() {
		return isComment;
	}

	public void setIsComment(Boolean isComment) {
		this.isComment = isComment;
	}

	public Boolean getIsCommentFollow() {
		return isCommentFollow;
	}

	public void setIsCommentFollow(Boolean isCommentFollow) {
		this.isCommentFollow = isCommentFollow;
	}

	public Boolean getIsMessage() {
		return isMessage;
	}

	public void setIsMessage(Boolean isMessage) {
		this.isMessage = isMessage;
	}

	public Boolean getIsMessageFollow() {
		return isMessageFollow;
	}

	public void setIsMessageFollow(Boolean isMessageFollow) {
		this.isMessageFollow = isMessageFollow;
	}

	public Boolean getIsWorks() {
		return isWorks;
	}

	public void setIsWorks(Boolean isWorks) {
		this.isWorks = isWorks;
	}

	public Boolean getIsWorksFollow() {
		return isWorksFollow;
	}

	public void setIsWorksFollow(Boolean isWorksFollow) {
		this.isWorksFollow = isWorksFollow;
	}

}