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
@TableName("bq_album")
@SuppressWarnings("serial")
public class Album extends BaseModel {

    /**
     * 主题ID
     */
	@TableField("subject_id")
	private String subjectId;
    /**
     * 层ID
     */
	@TableField("layer_id")
	private String layerId;
    /**
     * 用户ID
     */
	@TableField("user_id")
	private String userId;
    /**
     * 喜欢数量
     */
	@TableField("liked_num")
	private Integer likedNum;
    /**
     * 图片
     */
	@TableField("image_")
	private String image;
    /**
     * 图片类型
     */
	@TableField("image_type")
	private String imageType;
    /**
     * 图片宽度
     */
	@TableField("image_width")
	private Integer imageWidth;
    /**
     * 图片高度
     */
	@TableField("image_height")
	private Integer imageHeight;

	@TableField(exist = false)
	private boolean isLiked;

	@TableField(exist = false)
	private Layer layer;


	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getLikedNum() {
		return likedNum;
	}

	public void setLikedNum(Integer likedNum) {
		this.likedNum = likedNum;
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

	public boolean isLiked() {
		return isLiked;
	}

	public void setLiked(boolean liked) {
		isLiked = liked;
	}

	public Layer getLayer() {
		return layer;
	}

	public void setLayer(Layer layer) {
		this.layer = layer;
	}
}