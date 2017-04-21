package com.bq.service;

import com.bq.model.SysEmail;
import com.bq.core.base.BaseService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * @author Harvey.Wei
 *
 */
@Service
@CacheConfig(cacheNames = "sysEmail")
public class SysEmailService extends BaseService<SysEmail> {

}
