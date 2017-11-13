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
@TableName("bq_dynamic")
@SuppressWarnings("serial")
public class Dynamic extends BaseModel {

    /**
     * 主题ID 或者 转发ID
     */
	@TableField("val_id")
	private String valId;
    /**
     * 用户ID
     */
	@TableField("user_id")
	private String userId;
    /**
     * 动态类型:(1:主题动态,2:转发动态)
     */
	@TableField("type_")
	private String type;

	@TableField(exist = false)
	private Forward forward;
	@TableField(exist = false)
	private Subject subject;


	public String getValId() {
		return valId;
	}

	public void setValId(String valId) {
		this.valId = valId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Forward getForward() {
		return forward;
	}

	public void setForward(Forward forward) {
		this.forward = forward;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}
}