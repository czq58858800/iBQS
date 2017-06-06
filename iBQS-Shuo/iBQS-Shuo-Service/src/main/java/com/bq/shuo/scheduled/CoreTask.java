package com.bq.shuo.scheduled;

import com.bq.shuo.service.SessionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/6/2 0002.
 */
@Service
@EnableScheduling
public class CoreTask {
    private final Logger logger = LogManager.getLogger();

    @Autowired
    private SessionService sessionService;

    /** 定时清除会话信息 */
    @Scheduled(cron = "0 0/5 * * * *")
    public void cleanExpiredSessions() {
        logger.info("cleanExpiredSessions");
        sessionService.cleanExpiredSessions();
    }
}
