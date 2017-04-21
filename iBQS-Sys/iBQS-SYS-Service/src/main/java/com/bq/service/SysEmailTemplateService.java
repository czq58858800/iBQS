package com.bq.service;

import com.bq.core.base.BaseService;
import com.bq.model.SysEmailTemplate;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * @author Harvey.Wei
 *
 */
@Service
@CacheConfig(cacheNames = "sysEmailTemplate")
public class SysEmailTemplateService extends BaseService<SysEmailTemplate> {

}
