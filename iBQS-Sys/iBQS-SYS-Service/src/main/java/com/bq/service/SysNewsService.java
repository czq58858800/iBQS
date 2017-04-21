package com.bq.service;

import com.bq.model.SysNews;
import com.bq.core.base.BaseService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * @author Harvey.Wei
 *
 */
@Service
@CacheConfig(cacheNames = "sysNews")
public class SysNewsService extends BaseService<SysNews> {

}
