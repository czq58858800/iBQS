package com.bq.shuo.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bq.shuo.core.base.BaseModel;

import java.util.Date;
import java.util.List;


/**
 * <p>
 * 主题表
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@TableName("bq_subject")
@SuppressWarnings("serial")
public class Subject extends BaseModel {

    /**
     * 内容
     */
	@TableField("content_")
	private String content;
    /**
     * (Library)封面ID
     */
	@TableField("cover_")
	private String cover;
	@TableField("cover_hash")
	private String coverHash;
	@TableField("cover_type")
	private String coverType;
	@TableField("cover_width")
	private Integer coverWidth;
	@TableField("cover_height")
	private Integer coverHeight;

	@TableField("layer_keyword")
	private String layerKeyword;
    /**
     * 专辑数量
     */
	@TableField("album_num")
	private Integer albumNum;
    /**
     * 用户ID
     */
	@TableField("user_id")
	private String userId;
    /**
     * 纬度
     */
	@TableField("lat_")
	private Double lat;
    /**
     * 经度
     */
	@TableField("lng_")
	private Double lng;
    /**
     * 来源
     */
	@TableField("source_")
	private String source;
    /**
     * 客户端地址
     */
	@TableField("client_host")
	private String clientHost;

	@TableField("location_")
	private String location;
    /**
     * 是否显示位置
     */
	@TableField("is_location")
	private Boolean isLocation;
    /**
     * 热门权重
     */
	@TableField("is_hot")
	private Boolean isHot;

	/**
	 * 热门更新时间
	 */
	@TableField("hot_time")
	private Date hotTime;

	/**
	 * 是否允许改图
	 */
	@TableField(exist = false)
	private boolean isWorks;

	/**
	 * 是否允许评论
	 */
	@TableField(exist = false)
	private boolean isComment;

	/**
	 * 是否已喜欢
	 */
	@TableField(exist = false)
	private boolean isLiked;

	/**
	 * 表情作者
	 */
	@TableField(exist = false)
	private User user;

	/**
	 * 专辑
	 */
	@TableField(exist = false)
	private List<Album> albums;

	/**
	 * 转发数量
	 */
	@TableField(exist = false)
	private int forwardNum;


	/**
	 * 浏览数量
	 */
	@TableField(exist = false)
	private int viewNum;

	/**
	 * 评论数量
	 */
	@TableField(exist = false)
	private int commentsNum;

	/**
	 * 喜欢数量
	 */
	@TableField(exist = false)
	private int likedNum;

	public Subject(){}

	public Subject(String userId, String content, boolean isLocation, String location) {
		this.userId = userId;
		this.content = content;
		this.isLocation = isLocation;
		this.location = location;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getCoverHash() {
		return coverHash;
	}

	public void setCoverHash(String coverHash) {
		this.coverHash = coverHash;
	}

	public String getCoverType() {
		return coverType;
	}

	public void setCoverType(String coverType) {
		this.coverType = coverType;
	}

	public Integer getCoverWidth() {
		return coverWidth;
	}

	public void setCoverWidth(Integer coverWidth) {
		this.coverWidth = coverWidth;
	}

	public Integer getCoverHeight() {
		return coverHeight;
	}

	public void setCoverHeight(Integer coverHeight) {
		this.coverHeight = coverHeight;
	}

	public String getLayerKeyword() {
		return layerKeyword;
	}

	public void setLayerKeyword(String layerKeyword) {
		this.layerKeyword = layerKeyword;
	}

	public Integer getAlbumNum() {
		return albumNum;
	}

	public void setAlbumNum(Integer albumNum) {
		this.albumNum = albumNum;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getClientHost() {
		return clientHost;
	}

	public void setClientHost(String clientHost) {
		this.clientHost = clientHost;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Boolean getIsLocation() {
		return isLocation;
	}

	public void setIsLocation(Boolean isLocation) {
		this.isLocation = isLocation;
	}

	public Boolean getIsHot() {
		return isHot;
	}

	public void setIsHot(Boolean isHot) {
		this.isHot = isHot;
	}

	public Date getHotTime() {
		return hotTime;
	}

	public void setHotTime(Date hotTime) {
		this.hotTime = hotTime;
	}

	public boolean isWorks() {
		return isWorks;
	}

	public void setWorks(boolean works) {
		isWorks = works;
	}

	public boolean isComment() {
		return isComment;
	}

	public void setComment(boolean comment) {
		isComment = comment;
	}

	public boolean isLiked() {
		return isLiked;
	}

	public void setLiked(boolean liked) {
		isLiked = liked;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Album> getAlbums() {
		return albums;
	}

	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}

	public int getForwardNum() {
		return forwardNum;
	}

	public void setForwardNum(int forwardNum) {
		this.forwardNum = forwardNum;
	}

	public int getViewNum() {
		return viewNum;
	}

	public void setViewNum(int viewNum) {
		this.viewNum = viewNum;
	}

	public int getCommentsNum() {
		return commentsNum;
	}

	public void setCommentsNum(int commentsNum) {
		this.commentsNum = commentsNum;
	}

	public int getLikedNum() {
		return likedNum;
	}

	public void setLikedNum(int likedNum) {
		this.likedNum = likedNum;
	}
}