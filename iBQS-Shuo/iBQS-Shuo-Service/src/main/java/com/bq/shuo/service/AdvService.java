package com.bq.shuo.service;

import com.bq.shuo.core.base.BaseService;
import com.bq.shuo.model.Adv;
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
@CacheConfig(cacheNames = "adv")
public class AdvService extends BaseService<Adv> {
}