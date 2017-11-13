package com.bq.shuo.core.interceptor;

import com.alibaba.fastjson.JSON;
import com.bq.core.interceptor.BaseInterceptor;
import com.bq.core.util.DateUtil;
import com.bq.core.util.ExceptionUtil;
import com.bq.core.util.WebUtil;
import com.bq.shuo.core.base.BaseProvider;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.core.support.login.LoginHelper;
import com.bq.shuo.core.support.login.NoPwdAuthenticationToken;
import com.bq.shuo.model.ShuoEvent;
import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.io.ClassResolvingObjectInputStream;
import org.apache.shiro.io.DefaultSerializer;
import org.apache.shiro.io.SerializationException;
import org.apache.shiro.io.Serializer;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 日志拦截器
 * 
 * @author chern.zq
 * @version 2016年6月14日 下午6:18:46
 */
public class ShuoEventInterceptor extends BaseInterceptor {
	protected static Logger logger = LogManager.getLogger();

	private final ThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("ThreadLocal StartTime");
	private ExecutorService executorService = Executors.newCachedThreadPool();

	@Autowired
	@Qualifier("shuoProvider")
	protected BaseProvider provider;

	static UASparser uasParser = null;

	// 初始化uasParser对象
	static {
		try {
			uasParser = new UASparser(OnlineUpdater.getVendoredInputStream());
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 开始时间（该数据只有当前请求的线程可见）
		startTimeThreadLocal.set(System.currentTimeMillis());
		return super.preHandle(request, response, handler);
	}

	public void afterCompletion(final HttpServletRequest request, HttpServletResponse response, Object handler,
			final Exception ex) throws Exception {
		final Long startTime = startTimeThreadLocal.get();
		final Long endTime = System.currentTimeMillis();
		// 保存日志

		String userAgent = null;
		try {
			UserAgentInfo userAgentInfo = uasParser.parse(request.getHeader("user-agent"));
			userAgent = userAgentInfo.getOsName() + " " + userAgentInfo.getType() + " " + userAgentInfo.getUaName();
		} catch (IOException e) {
			logger.error("", e);
		}
		String path = request.getServletPath();
		if (!path.contains("/read/") && !path.contains("/unauthorized") && !path.contains("/forbidden")) {
			final ShuoEvent record = new ShuoEvent();
			String uid = WebUtil.getCurrentUser();
			record.setMethod(request.getMethod());
			record.setRequestUri(request.getServletPath());
			record.setClientHost(WebUtil.getHost(request));
			record.setUserAgent(userAgent);
			if (path.contains("/upload/")) {
				record.setParameters("");
			} else {
				record.setParameters(JSON.toJSONString(request.getParameterMap()));
			}
			record.setStatus(response.getStatus());
			final String msg = (String) request.getAttribute("msg");
			try {
				HandlerMethod handlerMethod = (HandlerMethod) handler;
				ApiOperation apiOperation = handlerMethod.getMethod().getAnnotation(ApiOperation.class);
				record.setTitle(apiOperation.value());
			} catch (Exception e) {
				logger.error("", e);
			}
			executorService.submit(new Runnable() {
				public void run() {
					try { // 保存操作
						if (StringUtils.isNotBlank(msg)) {
							record.setRemark(msg);
						} else {
							record.setRemark(ExceptionUtil.getStackTraceAsString(ex));
						}

						Parameter parameter = new Parameter("eventService", "update").setModel(record);
						provider.execute(parameter);

						// 内存信息
						if (logger.isDebugEnabled()) {
							String message = "开始时间: {}; 结束时间: {}; 耗时: {}s; URI: {}; ";
							// 最大内存: {}M; 已分配内存: {}M; 已分配内存中的剩余空间: {}M; 最大可用内存:
							// {}M.
							// long total = Runtime.getRuntime().totalMemory() /
							// 1024 / 1024;
							// long max = Runtime.getRuntime().maxMemory() /
							// 1024 / 1024;
							// long free = Runtime.getRuntime().freeMemory() /
							// 1024 / 1024;
							// , max, total, free, max - total + free
							logger.debug(message, DateUtil.format(startTime, "HH:mm:ss.SSS"),
									DateUtil.format(endTime, "HH:mm:ss.SSS"), (endTime - startTime) / 1000.00,
									record.getRequestUri());
						}
					} catch (Exception e) {
						logger.error("Save event log cause error :", e);
					}
				}
			});
		} else if (path.contains("/unauthorized")) {
			logger.warn("用户[{}]没有登录", WebUtil.getHost(request) + "@" + userAgent);
		} else if (path.contains("/forbidden")) {
			logger.warn("用户[{}]没有权限", WebUtil.getCurrentUser() + "@" + WebUtil.getHost(request) + "@" + userAgent);
		}
		super.afterCompletion(request, response, handler, ex);
	}

	public PrincipalCollection deserialize(byte[] serialized) {
		if(serialized == null) {
			String bais1 = "argument cannot be null.";
			throw new IllegalArgumentException(bais1);
		} else {
			ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
			BufferedInputStream bis = new BufferedInputStream(bais);

			try {
				ClassResolvingObjectInputStream e = new ClassResolvingObjectInputStream(bis);
				Object msg1 = e.readObject();
				e.close();
				return (PrincipalCollection) msg1;
			} catch (Exception var6) {
				String msg = "Unable to deserialze argument byte array.";
				throw new SerializationException(msg, var6);
			}
		}
	}

	private String ensurePadding(String base64) {
		int length = base64.length();
		if(length % 4 != 0) {
			StringBuilder sb = new StringBuilder(base64);

			for(int i = 0; i < length % 4; ++i) {
				sb.append('=');
			}

			base64 = sb.toString();
		}

		return base64;
	}
}
