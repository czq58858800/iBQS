package com.bq.shuo.service;

import com.bq.core.Constants;
import com.bq.shuo.model.Feedback;
import com.bq.shuo.core.base.BaseService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 意见反馈表  服务实现类
 * </p>
 *
 * @author chern.zq
 * @since 2017-04-13
 */
@Service
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"feedback")
public class FeedbackService extends BaseService<Feedback> {
	
}