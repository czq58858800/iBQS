package com.bq.shuo.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bq.shuo.core.base.BaseModel;
import java.io.Serializable;


/**
 * <p>
 * 分类表
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@TableName("bq_category_review")
@SuppressWarnings("serial")
public class CategoryReview extends BaseModel {

	@TableField("category_id")
	private String categoryId;
    /**
     * 分类封面
     */
	@TableField("cover_")
	private String cover;
    /**
     * 封面图片类型
     */
	@TableField("cover_type")
	private String coverType;
    /**
     * 封面图片宽度
     */
	@TableField("cover_width")
	private Integer coverWidth;
    /**
     * 封面图片高度
     */
	@TableField("cover_height")
	private Integer coverHeight;
    /**
     * 名称
     */
	@TableField("name_")
	private String name;
    /**
     * 分类描述
     */
	@TableField("summary_")
	private String summary;
    /**
     * 用户ID
     */
	@TableField("user_id")
	private String userId;
    /**
     * 标签
     */
	@TableField("tags_")
	private String tags;
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

	public CategoryReview() {

	}

	public CategoryReview(String categoryId, String cover, String name, String tags, String summary,String userId) {
		this.categoryId = categoryId;
		this.cover=cover;
		this.name=name;
		this.tags=tags;
		this.summary=summary;
		this.userId=userId;
		this.audit="1";
	}


	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
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