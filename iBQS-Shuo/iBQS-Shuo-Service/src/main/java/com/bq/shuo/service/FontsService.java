package com.bq.shuo.service;

import com.bq.core.Constants;
import com.bq.shuo.model.Fonts;
import com.bq.shuo.core.base.BaseService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * <p>
 *   服务实现类
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@Service
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"fonts")
public class FontsService extends BaseService<Fonts> {
	
}