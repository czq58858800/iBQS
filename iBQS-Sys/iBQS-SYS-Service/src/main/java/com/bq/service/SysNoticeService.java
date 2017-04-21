package com.bq.service;

import com.bq.model.SysNotice;
import com.bq.core.base.BaseService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * @author Harvey.Wei
 *
 */
@Service
@CacheConfig(cacheNames = "sysNoticeTemplate")
public class SysNoticeService extends BaseService<SysNotice> {

}
