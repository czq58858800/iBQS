package com.bq.core.support;

import com.bq.core.config.Resources;

/**
 * Ajax 请求时的自定义查询状态码，主要参考Http状态码，但并不完全对应
 * 
 * @author Harvey.Wei
 * @version 2016年5月20日 下午3:19:19
 */
public enum HttpCode {
	/** 200请求成功 */
	OK(200),
	/** 207频繁操作 */
	MULTI_STATUS(207),
	/** 303登录失败 */
	LOGIN_FAIL(303),
	/** 400请求参数出错 */
	BAD_REQUEST(400),
	/** 401没有登录 */
	UNAUTHORIZED(401),
	/** 403没有权限 */
	FORBIDDEN(403),
	/** 404找不到页面 */
	NOT_FOUND(404),
	/** 405暂无数据 */
	NOT_DATA(405),
	/** 408请求超时 */
	REQUEST_TIMEOUT(408),
	/** 409发生冲突 */
	CONFLICT(409),
	/** 410已被删除 */
	GONE(410),
	/** 423已被锁定 */
	LOCKED(423),
	/** 430未知类型 */
	UNKNOWN_TYPE(430),
	/** 431未知手机号*/
	UNKNOWN_PHONE(431),
	/** 500服务器出错 */
	INTERNAL_SERVER_ERROR(500),
	/** 手机未注册 */
	REG_NO_HAS_PHONE(2001),
	/** 昵称未使用 */
	REG_NO_HAS_NAME(2002),
	/** 手机已注册 */
	REG_HAS_PHONE(2003),
	/** 昵称已被使用 */
	REG_HAS_NAME(2004),
	/** 旧密码不正确 */
	PASSWORD_NOT_CONSISTENT(2005),
	/** 用户不存在 */
	USER_NON_EXISTENT(2006),
	/** 用户已被绑定 */
	USER_HAS_BOUND(2007),
	/** 未绑定其他用户不允许解除绑定*/
	USER_NO_BOUND(2107),
	/** 已在黑名单 */
	ALREADY_IN_BLACKLIST(2008),
	/** 不在黑名单 */
	NO_BLACKLIST(2009),
	/** 2010已喜欢 */
	HAS_LIKED(2010),
	/** 2011未喜欢 */
	NOT_LIKED(2011),
	/** 2012不允许关注自己 */
	DONT_FOLLOW(2012),
	/** 2013已关注 */
	HAS_FOLLOW(2013),
	/** 2014未关注 */
	NOT_FOLLOW(2014),
	/** 2015关注数量超过2000 */
	LIMIT_OF_ATTENTION(2015),
	/** 2016贴纸上传数量超过20 */
	LIMIT_OF_STICKER(2016),
	/** 2017未发布主题 */
	UNRELEASED_SUBJECT(2017),
	/** 2018未开放申请 */
	NOT_ALLOW_APPLY_TOPIC(2018),
	/** 2101表情已存在*/
	SUBJECT_EXIST(2101),
	/** 4001帐号不存在 */
	USER_ACCOUNT_NOT_EXIST(4001),
	/** 4002验证码不正确 */
	SMS_CAPTCHA_INCORRECT(4002),
	/** 4003验证吗失效 */
	SMS_CAPTCHA_FAIL(4003);


	private final Integer value;

	private HttpCode(Integer value) {
		this.value = value;
	}

	/**
	 * Return the integer value of this status code.
	 */
	public Integer value() {
		return this.value;
	}

	public String msg() {
		return Resources.getMessage("HTTPCODE_" + this.value);
	}

	public String toString() {
		return this.value.toString();
	}
}
