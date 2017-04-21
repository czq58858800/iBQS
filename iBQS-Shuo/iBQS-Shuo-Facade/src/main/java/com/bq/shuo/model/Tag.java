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
@TableName("bq_tag")
@SuppressWarnings("serial")
public class Tag extends BaseModel {

    /**
     * 名称
     */
	@TableField("name_")
	private String name;
	@TableField("code_")
	private Integer code;
    /**
     * 排序
     */
	@TableField("sort_")
	private Integer sort;
    /**
     * 类型:(1.特殊标签;2.普通标签)
     */
	@TableField("type_")
	private String type;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}