package com.bq.shuo.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bq.shuo.core.base.BaseModel;



/**
 * <p>
 * 
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@TableName("bq_adv")
@SuppressWarnings("serial")
public class Adv extends BaseModel {

    /**
     * 广告图片
     */
	@TableField("image_")
	private String image;
    /**
     * 广告名称
     */
	@TableField("name_")
	private String name;
    /**
     * 广告URI:
     */
	@TableField("url_")
	private String url;
    /**
     * 广告排序
     */
	@TableField("sort_no")
	private Integer sortNo;
    /**
     * 广告类型：(1:发现;2:热门素材;3:新品素材)
     */
	@TableField("type_")
	private String type;


	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}