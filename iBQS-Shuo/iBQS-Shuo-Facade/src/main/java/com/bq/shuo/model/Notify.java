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
@TableName("bq_notify")
@SuppressWarnings("serial")
public class Notify extends BaseModel {

	@TableField("content_")
	private String content;
	@TableField("send_user_id")
	private String sendUserId;
	@TableField("receive_user_id")
	private String receiveUserId;
	@TableField("subject_id")
	private String subjectId;
	@TableField("comments_id")
	private String commentsId;
	@TableField("is_push")
	private Boolean isPush;


    /**
     * 消息类型：（1:评论；2:@；3：转发；4：喜欢）
     */
	@TableField("msg_type")
	private String msgType;


	@TableField(exist = false)
	private Subject subject;

	@TableField(exist = false)
	private User sendUser;

	public Notify(){}

	public Notify(String sendUserId, String receiveUserId, String subjectId,String commentsId, String content, String msgType) {
		this.sendUserId = sendUserId;
		this.receiveUserId = receiveUserId;
		this.subjectId = subjectId;
		this.commentsId = commentsId;
		this.content = content;
		this.msgType = msgType;
		this.isPush = false;
	}

    public Notify(String sendUserId, String receiveUserId, String subjectId, String msgType) {
		this.sendUserId = sendUserId;
		this.receiveUserId = receiveUserId;
		this.subjectId = subjectId;
		this.msgType = msgType;
    }

	public Notify(String sendUserId, String subjectId, String commentsId) {
		this.sendUserId = sendUserId;
		this.subjectId = subjectId;
		this.commentsId = commentsId;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	public String getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getCommentsId() {
		return commentsId;
	}

	public void setCommentsId(String commentsId) {
		this.commentsId = commentsId;
	}

	public Boolean getIsPush() {
		return isPush;
	}

	public void setIsPush(Boolean isPush) {
		this.isPush = isPush;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public User getSendUser() {
		return sendUser;
	}

	public void setSendUser(User sendUser) {
		this.sendUser = sendUser;
	}
}