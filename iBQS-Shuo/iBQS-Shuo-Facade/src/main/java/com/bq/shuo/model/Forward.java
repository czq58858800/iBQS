package com.bq.shuo.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bq.shuo.core.base.BaseModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;


/**
 * <p>
 * 
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@TableName("bq_forward")
@SuppressWarnings("serial")
public class Forward extends BaseModel {

    /**
     * 主题ID
     */
	@TableField("subject_id")
	private String subjectId;
    /**
     * 用户ID
     */
	@TableField("user_id")
	private String userId;
    /**
     * 内容
     */
	@TableField("content_")
	private String content;

	@TableField(exist = false)
	private Subject subject;

	@TableField(exist = false)
	private User user;


	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}