package com.bq.shuo.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bq.shuo.core.base.BaseModel;
import java.io.Serializable;


/**
 * <p>
 * 主题关注表
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@TableName("bq_subject_liked")
@SuppressWarnings("serial")
public class SubjectLiked extends BaseModel {

    /**
     * 用户ID
     */
	@TableField("user_id")
	private String userId;
    /**
     * 主题ID
     */
	@TableField("subject_id")
	private String subjectId;

	@TableField(exist = false)
	private Subject subject;

	public SubjectLiked(){}

	public SubjectLiked(String userId,String subjectId){
		this.userId = userId;
		this.subjectId = subjectId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

}