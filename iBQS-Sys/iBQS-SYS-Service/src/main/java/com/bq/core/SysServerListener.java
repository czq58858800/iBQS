package com.bq.core;

import javax.servlet.ServletContextEvent;

import com.bq.core.listener.ServerListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.bq.service.SysCacheService;
import com.bq.service.SysDicService;
import com.bq.service.SysUserService;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class SysServerListener extends ServerListener {
	protected final Logger logger = LogManager.getLogger(this.getClass());

	public void contextInitialized(ServletContextEvent contextEvent) {
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		context.getBean(SysCacheService.class).flush();
		context.getBean(SysUserService.class).init();
		SysDicService sysDicService = context.getBean(SysDicService.class);
		sysDicService.getAllDic();
		super.contextInitialized(contextEvent);
	}
}