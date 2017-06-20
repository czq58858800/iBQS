package com.bq.shuo.service;

import com.bq.core.Constants;
import com.bq.shuo.model.Layer;
import com.bq.shuo.core.base.BaseService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 图层表  服务实现类
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@Service
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"layer")
public class LayerService extends BaseService<Layer> {

}