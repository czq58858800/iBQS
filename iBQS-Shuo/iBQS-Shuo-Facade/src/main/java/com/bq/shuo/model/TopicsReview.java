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

	@TableField("topic_id")
	private String topicId;
    /**
     * 封面
     */
	@TableField("cover_")
	private String cover;
	@TableField("cover_type")
	private String coverType;
	@TableField("cover_width")
	private Integer coverWidth;
	@TableField("cover_height")
	private Integer coverHeight;
    /**
     * 名称
     */
	@TableField("name_")
	private String name;
	@TableField("owner_id")
	private String ownerId;
    /**
     * 简介
     */
	@TableField("summary_")
	private String summary;
    /**
     * 审核状态(-1：审核失败；0：上传素材中；1：审核中；2：审核通过）
     */
	@TableField("audit_")
	private String audit;
    /**
     * 审核用户ID
     */
	@TableField("audit_user_id")
	private String auditUserId;

	public TopicsReview(){}

	public TopicsReview(String topicId,String name, String ownerId, String cover, String coverType, Integer coverWidth, Integer coverHeight, String summary) {
		this.topicId = topicId;
		this.name = name;
		this.ownerId = ownerId;
		this.cover = cover;
		this.coverType = coverType;
		this.coverWidth = coverWidth;
		this.coverHeight = coverHeight;
		this.summary = summary;
		this.audit = "0";
	}


	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getCoverType() {
		return coverType;
	}

	public void setCoverType(String coverType) {
		this.coverType = coverType;
	}

	public Integer getCoverWidth() {
		return coverWidth;
	}

	public void setCoverWidth(Integer coverWidth) {
		this.coverWidth = coverWidth;
	}

	public Integer getCoverHeight() {
		return coverHeight;
	}

	public void setCoverHeight(Integer coverHeight) {
		this.coverHeight = coverHeight;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

}