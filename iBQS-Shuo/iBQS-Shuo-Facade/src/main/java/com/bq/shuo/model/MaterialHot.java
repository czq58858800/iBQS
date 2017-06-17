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
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@TableName("bq_material_hot")
@SuppressWarnings("serial")
public class MaterialHot extends BaseModel {

	@TableField("value_")
	private String value;
	/**类别:(0:全部;1:贴纸;2:素材)*/
	@TableField("type_")
	private String type;
	@TableField("sort_")
	private Integer sort;


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

}