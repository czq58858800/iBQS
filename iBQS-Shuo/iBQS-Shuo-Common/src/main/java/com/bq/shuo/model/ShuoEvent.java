package com.bq.shuo.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bq.shuo.core.base.BaseModel;


/**
 * <p>
 * 
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-11
 */
@TableName("shuo_event")
@SuppressWarnings("serial")
public class ShuoEvent extends BaseModel {

    /**
     * 标题
     */
	@TableField("title_")
	private String title;
    /**
     * 请求地址
     */
	@TableField("request_uri")
	private String requestUri;
    /**
     * 请求参数
     */
	@TableField("parameters_")
	private String parameters;
    /**
     * 请求方法
     */
	@TableField("method_")
	private String method;
    /**
     * 客户端地址
     */
	@TableField("client_host")
	private String clientHost;
    /**
     * 用户代理
     */
	@TableField("user_agent")
	private String userAgent;
    /**
     * 请求状态
     */
	@TableField("status_")
	private Integer status;
	@TableField("create_by")
	private Long createBy;
	@TableField("update_by")
	private Long updateBy;


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getClientHost() {
		return clientHost;
	}

	public void setClientHost(String clientHost) {
		this.clientHost = clientHost;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

}