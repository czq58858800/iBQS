package com.bq.service;

import com.bq.core.base.BaseService;
import com.bq.model.SysEmailConfig;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * @author Harvey.Wei
 *
 */
@Service
@CacheConfig(cacheNames = "sysEmailConfig")
public class SysEmailConfigService extends BaseService<SysEmailConfig> {

}
