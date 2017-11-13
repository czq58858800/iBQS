package com.bq.shuo.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bq.shuo.core.base.BaseModel;
import java.io.Serializable;


/**
 * <p>
 * 素材表
 * </p>
 *
 * @author chern.zq
 * @since 2017-04-13
 */
@TableName("bq_material")
@SuppressWarnings("serial")
public class Material extends BaseModel {

	@TableField("name_")
	private String name;

	@TableField("user_id")
	private String userId;
    /**
     * 分类ID
     */
	@TableField("category_id")
	private String categoryId;
    /**
     * 素材地址
     */
	@TableField("image_")
	private String image;
	@TableField("image_type")
	private String imageType;
	@TableField("image_width")
	private Integer imageWidth;
	@TableField("image_height")
	private Integer imageHeight;
    /**
     * 引用次数
     */
	@TableField("citations_")
	private Integer citations;

	@TableField("is_cover")
	private Boolean isCover;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public Integer getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(Integer imageWidth) {
		this.imageWidth = imageWidth;
	}

	public Integer getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(Integer imageHeight) {
		this.imageHeight = imageHeight;
	}

	public Integer getCitations() {
		return citations;
	}

	public void setCitations(Integer citations) {
		this.citations = citations;
	}

	public Boolean getIsCover() {
		return isCover == null ? false : isCover;
	}

	public void setIsCover(Boolean isCover) {
		this.isCover = isCover;
	}
}