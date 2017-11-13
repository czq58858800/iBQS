package com.bq.shuo.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bq.shuo.core.base.BaseModel;
import java.io.Serializable;


/**
 * <p>
 * 评论表
 * </p>
 *
 * @author chern.zq
 * @since 2017-04-13
 */
@TableName("bq_comments")
@SuppressWarnings("serial")
public class Comments extends BaseModel {

    /**
     * 主题ID
     */
	@TableField("subject_id")
	private String subjectId;
    /**
     * 评论用户ID
     */
	@TableField("user_id")
	private String userId;
    /**
     * 被回复者用户ID
     */
	@TableField("be_reply_id")
	private String beReplyId;
    /**
     * 回复评论ID
     */
	@TableField("be_reply_comment_id")
	private String beReplyCommentId;
    /**
     * 评论内容
     */
	@TableField("content_")
	private String content;

	@TableField(exist = false)
	private int praiseNum;
	@TableField(exist = false)
	private User replyUser;
	@TableField(exist = false)
	private User beReplyUser;
	@TableField(exist = false)
	private boolean isPraise;
	@TableField(exist = false)
	private Comments beReplyComment;

	public Comments() {}

    public Comments(String subjectId, String content, String beReplyId, String beReplyCommentId) {
    	this.subjectId = subjectId;
    	this.content = content;
    	this.beReplyId = beReplyId;
    	this.beReplyCommentId = beReplyCommentId;
    }


    public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBeReplyId() {
		return beReplyId;
	}

	public void setBeReplyId(String beReplyId) {
		this.beReplyId = beReplyId;
	}

	public String getBeReplyCommentId() {
		return beReplyCommentId;
	}

	public void setBeReplyCommentId(String beReplyCommentId) {
		this.beReplyCommentId = beReplyCommentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(int praiseNum) {
		this.praiseNum = praiseNum;
	}

	public User getReplyUser() {
		return replyUser;
	}

	public void setReplyUser(User replyUser) {
		this.replyUser = replyUser;
	}

	public User getBeReplyUser() {
		return beReplyUser;
	}

	public void setBeReplyUser(User beReplyUser) {
		this.beReplyUser = beReplyUser;
	}

	public boolean isPraise() {
		return isPraise;
	}

	public void setPraise(boolean praise) {
		isPraise = praise;
	}

	public Comments getBeReplyComment() {
		return beReplyComment;
	}

	public void setBeReplyComment(Comments beReplyComment) {
		this.beReplyComment = beReplyComment;
	}
}