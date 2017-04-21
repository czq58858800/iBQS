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
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@TableName("bq_feedback")
@SuppressWarnings("serial")
public class Feedback extends BaseModel {

    /**
     * 用户ID
     */
	@TableField("user_id")
	private Long userId;
    /**
     * 提交内容
     */
	@TableField("sub_content")
	private String subContent;
    /**
     * 回复内容
     */
	@TableField("reply_content")
	private String replyContent;
    /**
     * 系统用户ID
     */
	@TableField("sys_user_id")
	private Long sysUserId;
    /**
     * 回复时间
     */
	@TableField("reply_time")
	private Date replyTime;


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
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

	public Long getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(Long sysUserId) {
		this.sysUserId = sysUserId;
	}

	public Date getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}

}