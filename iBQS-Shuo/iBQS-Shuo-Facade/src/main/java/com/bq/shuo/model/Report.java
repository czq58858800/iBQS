package com.bq.shuo.model;

import java.util.Date;
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
@TableName("bq_report")
@SuppressWarnings("serial")
public class Report extends BaseModel {

	@TableField("user_id")
	private String userId;
	@TableField("val_id")
	private String valId;
	@TableField("sys_user_id")
	private String sysUserId;
    /**
     * 描述
     */
	@TableField("sub_content")
	private String subContent;
	@TableField("reply_content")
	private String replyContent;
	@TableField("reply_time")
	private Date replyTime;
    /**
     * 举报:(1:表情;2:评论;3:用户;)
     */
	@TableField("type_")
	private String type;
    /**
     * 状态:(0:已处理;1:处理中)
     */
	@TableField("status_")
	private String status;

	@TableField(exist = false)
	private String valName;
	@TableField(exist = false)
	private String userName;


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getValId() {
		return valId;
	}

	public void setValId(String valId) {
		this.valId = valId;
	}

	public String getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
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

	public Date getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getValName() {
		return valName;
	}

	public void setValName(String valName) {
		this.valName = valName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}