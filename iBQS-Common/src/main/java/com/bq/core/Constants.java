package com.bq.core;

import java.util.Map;

import com.bq.core.util.InstanceUtil;

/**
 * 常量表
 * 
 * @author Harvey.Wei
 * @version $Id: Constants.java, v 0.1 2014-2-28 上午11:18:28 Harvey.Wei Exp $
 */
public interface Constants {
    /**
     * 异常信息统一头信息<br>
     * 非常遗憾的通知您,程序发生了异常
     */
    public static final String Exception_Head = "OH,MY GOD! SOME ERRORS OCCURED! AS FOLLOWS :";
    /** 缓存键值 */
    public static final Map<Class<?>, String> cacheKeyMap = InstanceUtil.newHashMap();
    /** 操作名称 */
    public static final String OPERATION_NAME = "OPERATION_NAME";
    /** 客户端语言 */
    public static final String USERLANGUAGE = "userLanguage";
    /** 客户端主题 */
    public static final String WEBTHEME = "webTheme";
    /** 当前用户 */
    public static final String CURRENT_USER = "CURRENT_USER";
    /** 上次请求地址 */
    public static final String PREREQUEST = "PREREQUEST";
    /** 上次请求时间 */
    public static final String PREREQUEST_TIME = "PREREQUEST_TIME";
    /** 登录地址 */
    public static final String LOGIN_URL = "/login.html";
    /** 非法请求次数 */
    public static final String MALICIOUS_REQUEST_TIMES = "MALICIOUS_REQUEST_TIMES";
    /** 缓存命名空间 */
    public static final String CACHE_NAMESPACE = "iBQS:";
    /** 缓存命名空间 */
    public static final String CACHE_SHUO_NAMESPACE = "SHUO:";
    /** 在线用户数量 */
    public static final String ALLUSER_NUMBER = "SYSTEM:" + CACHE_NAMESPACE + "ALLUSER_NUMBER";
    /** TOKEN */
    public static final String TOKEN_KEY = CACHE_NAMESPACE + "TOKEN_KEY";
    /** 系統配置 */
    public static final String SYSTEM_CONFIG_CACHA = CACHE_NAMESPACE+"SYSTEM_CONFIG";

    /** 日志表状态 */
    public interface JOBSTATE {
        /**
         * 日志表状态，初始状态，插入
         */
        public static final String INIT_STATS = "I";
        /**
         * 日志表状态，成功
         */
        public static final String SUCCESS_STATS = "S";
        /**
         * 日志表状态，失败
         */
        public static final String ERROR_STATS = "E";
        /**
         * 日志表状态，未执行
         */
        public static final String UN_STATS = "N";
    }

    // 验证吗缓存
    public static String JR_SMS_CAPTCHA = "SMS_CAPTCHA:";
}
