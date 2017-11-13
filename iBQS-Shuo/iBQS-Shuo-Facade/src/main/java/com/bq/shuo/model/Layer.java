package com.bq.shuo.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bq.shuo.core.base.BaseModel;
import java.io.Serializable;


/**
 * <p>
 * 图层表
 * </p>
 *
 * @author chern.zq
 * @since 2017-04-13
 */
@TableName("bq_layer")
@SuppressWarnings("serial")
public class Layer extends BaseModel {

	@TableField("subject_id")
	private String subjectId;

    /**
     * 层信息
     */
	@TableField("layer_")
	private String layer;
    /**
     * 用户ID
     */
	@TableField("user_id")
	private String userId;

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getLayer() {
		return layer;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}