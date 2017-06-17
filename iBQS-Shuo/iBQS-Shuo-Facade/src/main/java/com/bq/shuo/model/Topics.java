package com.bq.shuo.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bq.shuo.core.base.BaseModel;

import java.util.Date;


/**
 * <p>
 * 话题表
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@TableName("bq_topics")
@SuppressWarnings("serial")
public class Topics extends BaseModel {

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

	@TableField("tags_")
	private String tags;

	@TableField("banner_")
	private String banner;
    /**
     * 名称
     */
	@TableField("name_")
	private String name;
	@TableField("owner_id")
	private String ownerId;
	/**
	 * 主持人状态：(-1:不允许申请;1:所有人;2:认证用户):
	 */
	@TableField("owner_status")
	private Integer ownerStatus;
    /**
     * 简介
     */
	@TableField("summary_")
	private String summary;
    /**
     * 话题数量
     */
	@TableField("view_num")
	private Integer viewNum;
    /**
     * 热门权重
     */
	@TableField("is_hot")
	private Boolean isHot;
	@TableField("hot_time")
	private Date hotTime;
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

	@TableField(exist = false)
	private User owner;

	public Topics() {

	}
    public Topics(String name, String cover, String summary) {
        this.name = name;
        this.cover = cover;
        this.summary = summary;
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

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
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

	public Integer getViewNum() {
		return viewNum;
	}

	public void setViewNum(Integer viewNum) {
		this.viewNum = viewNum;
	}

	public Boolean getIsHot() {
		return isHot;
	}

	public void setIsHot(Boolean isHot) {
		this.isHot = isHot;
	}

	public Date getHotTime() {
		return hotTime;
	}

	public void setHotTime(Date hotTime) {
		this.hotTime = hotTime;
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

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Integer getOwnerStatus() {
		return ownerStatus;
	}

	public void setOwnerStatus(Integer ownerStatus) {
		this.ownerStatus = ownerStatus;
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}
}