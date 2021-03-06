/**
 * 
 */
package com.bq.core.support.scheduler.job;

import com.bq.core.base.Parameter;
import com.bq.core.support.scheduler.TaskScheduled;
import com.bq.core.base.BaseProvider;
import com.bq.core.util.DubboUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

/**
 * 默认调度(非阻塞)
 * 
 * @author chern.zq
 * @version 2016年12月29日 上午11:52:32
 */
public class BaseJob implements Job {
	private Logger logger = LogManager.getLogger(this.getClass());

	public void execute(JobExecutionContext context) throws JobExecutionException {
		long start = System.currentTimeMillis();
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		String taskType = jobDataMap.getString("taskType");
		String targetObject = jobDataMap.getString("targetObject");
		String targetMethod = jobDataMap.getString("targetMethod");
		logger.info("定时任务开始执行: [{}.{}]", targetObject, targetMethod);
		try {
			ApplicationContext applicationContext = (ApplicationContext) context.getScheduler().getContext()
					.get("applicationContext");
			if (TaskScheduled.TaskType.local.equals(taskType)) {
				Object refer = applicationContext.getBean(targetObject);
				refer.getClass().getDeclaredMethod(targetMethod).invoke(refer);
			} else if (TaskScheduled.TaskType.dubbo.equals(taskType)) {
				String system = jobDataMap.getString("targetSystem");
				BaseProvider provider = (BaseProvider) DubboUtil.refer(applicationContext, system);
				provider.execute(new Parameter(targetObject, targetMethod));
			}
			double time = (System.currentTimeMillis() - start) / 1000.0;
			logger.info("定时任务[{}.{}]用时：{}s", targetObject, targetMethod, time);
		} catch (Exception e) {
			throw new JobExecutionException(e);
		}
	}
}
