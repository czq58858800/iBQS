package com.bq.shuo.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bq.shuo.core.base.BaseModel;

import java.util.Date;
import java.util.List;


/**
 * <p>
 * 分类表
 * </p>
 *
 * @author chern.zq
 * @since 2017-04-13
 */
@TableName("bq_category")
@SuppressWarnings("serial")
public class Category extends BaseModel {

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
     * 类别:(0:全部;1:贴纸;2:素材)
     */
	@TableField("type_")
	private Integer type;
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
	@TableField("view_num")
	private Integer viewNum;
    /**
     * 素材数量
     */
	@TableField("stuff_num")
	private Integer stuffNum;
    /**
     * 引用次数
     */
	@TableField("citations_")
	private Integer citations;
    /**
     * 排序
     */
	@TableField("sort_num")
	private Integer sortNum;

	/**
	 * 热门权重
	 */
	@TableField("is_hot")
	private Boolean isHot;

	/**
	 * 热门更新时间
	 */
	@TableField("hot_time")
	private Date hotTime;


	@TableField(exist = false)
	private int collNum;

	@TableField(exist = false)
	private boolean isColl;

	@TableField(exist = false)
	private User author;
	@TableField(exist = false)
	private List<Material> stuffs;

	public Category() {

	}
	public Category(String id, String cover, String name, String tags, String summary) {
		super.setId(id);
		this.cover = cover;
		this.name = name;
		this.tags = tags;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public Integer getViewNum() {
		return viewNum;
	}

	public void setViewNum(Integer viewNum) {
		this.viewNum = viewNum;
	}

	public Integer getStuffNum() {
		return stuffNum;
	}

	public void setStuffNum(Integer stuffNum) {
		this.stuffNum = stuffNum;
	}

	public Integer getCitations() {
		return citations;
	}

	public void setCitations(Integer citations) {
		this.citations = citations;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
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

	public int getCollNum() {
		return collNum;
	}

	public void setCollNum(int collNum) {
		this.collNum = collNum;
	}

	public boolean isColl() {
		return isColl;
	}

	public void setColl(boolean coll) {
		isColl = coll;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public List<Material> getStuffs() {
		return stuffs;
	}

	public void setStuffs(List<Material> stuffs) {
		this.stuffs = stuffs;
	}
}