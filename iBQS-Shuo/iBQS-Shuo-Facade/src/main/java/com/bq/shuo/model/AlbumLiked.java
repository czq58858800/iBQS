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
@TableName("bq_album_liked")
@SuppressWarnings("serial")
public class AlbumLiked extends BaseModel {

    /**
     * 专辑ID
     */
	@TableField("album_id")
	private String albumId;
    /**
     * 用户ID
     */
	@TableField("user_id")
	private String userId;
    /**
     * 排序
     */
	@TableField("sort_no")
	private Integer sortNo;

	@TableField(exist = false)
	private Album album;

	@TableField(exist = false)
	private Subject subject;

	public AlbumLiked(){}


	public AlbumLiked(String albumId,String userId) {
		this.albumId = albumId;
		this.userId = userId;
	}


	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}
}