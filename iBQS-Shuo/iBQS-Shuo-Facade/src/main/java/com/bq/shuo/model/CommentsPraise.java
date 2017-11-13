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
@TableName("bq_comments_praise")
@SuppressWarnings("serial")
public class CommentsPraise extends BaseModel {

    /**
     * 评论ID
     */
	@TableField("comment_id")
	private Long commentId;
    /**
     * 用户ID
     */
	@TableField("user_id")
	private Long userId;


	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}