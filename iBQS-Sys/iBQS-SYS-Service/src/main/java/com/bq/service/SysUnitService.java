package com.bq.service;

import com.bq.model.SysUnit;
import com.bq.core.base.BaseService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * @author chern.zq
 *
 */
@Service
@CacheConfig(cacheNames = "sysUnit")
public class SysUnitService extends BaseService<SysUnit> {

}
