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
@TableName("bq_category_collection")
@SuppressWarnings("serial")
public class CategoryCollection extends BaseModel {

    /**
     * 用户ID
     */
	@TableField("user_id")
	private String userId;
    /**
     * 分类ID
     */
	@TableField("category_id")
	private String categoryId;
	@TableField("sort_no")
	private Integer sortNo;

	@TableField(exist = false)
	private Category category;


	@TableField(exist = false)
	private User user;

	public CategoryCollection(){}

	public CategoryCollection(String userId, String categoryId){
		this.userId = userId;
		this.categoryId = categoryId;
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

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}