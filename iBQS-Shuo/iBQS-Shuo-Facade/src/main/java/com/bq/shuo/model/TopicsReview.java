package com.bq.shuo.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bq.shuo.core.base.BaseModel;
import java.io.Serializable;


/**
 * <p>
 * 话题表
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@TableName("bq_topics_review")
@SuppressWarnings("serial")
public class TopicsReview extends BaseModel {

	/**
	 * 话题ID
	 */
	@TableField("topic_id")
	private String topicId;
	/**
	 * 话题原因
	 */
	@TableField("reason_")
	private String reason;
	/**
	 * 话题主持人
	 */
	@TableField("owner_id")
	private String ownerId;
    /**
     * 简介
     */
	@TableField("summary_")
	private String summary;
    /**
     * 审核状态(-1:审核失败；1:审核中；2:审核通过）
     */
	@TableField("audit_")
	private String audit;
    /**
     * 审核用户ID
     */
	@TableField("audit_user_id")
	private String auditUserId;

	@TableField(exist = false)
	private Topics topics;

	@TableField(exist = false)
	private User owner;

	public TopicsReview(){}

	public TopicsReview(String topicId,String reason, String ownerId, String summary) {
		this.topicId = topicId;
		this.reason = reason;
		this.ownerId = ownerId;
		this.summary = summary;
		this.audit = "1";
		this.setEnable(true);
	}


	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getAudit() {
		return audit;
	}

	public void setAudit(String audit) {
		this.audit = audit;
	}

	public String getAuditUserId() {
		return auditUserId;
	}

	public void setAuditUserId(String auditUserId) {
		this.auditUserId = auditUserId;
	}

	public Topics getTopics() {
		return topics;
	}

	public void setTopics(Topics topics) {
		this.topics = topics;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
}