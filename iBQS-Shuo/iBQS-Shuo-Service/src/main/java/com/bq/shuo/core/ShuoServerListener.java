package com.bq.shuo.core;

import com.bq.shuo.service.SystemConfigService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.bq.core.listener.ServerListener;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContextEvent;

public class ShuoServerListener extends ServerListener {
	protected final Logger logger = LogManager.getLogger(this.getClass());

	public void contextInitialized(ServletContextEvent contextEvent) {
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
//		context.getBean(SysCacheService.class).flush();
		context.getBean(SystemConfigService.class).init();
//		SysDicService sysDicService = context.getBean(SysDicService.class);
//		sysDicService.getAllDic();
		super.contextInitialized(contextEvent);
	}
}