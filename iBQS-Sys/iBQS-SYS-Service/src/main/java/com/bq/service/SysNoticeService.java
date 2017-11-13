package com.bq.service;

import com.bq.model.SysNotice;
import com.bq.core.base.BaseService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * @author chern.zq
 *
 */
@Service
@CacheConfig(cacheNames = "sysNoticeTemplate")
public class SysNoticeService extends BaseService<SysNotice> {

}
