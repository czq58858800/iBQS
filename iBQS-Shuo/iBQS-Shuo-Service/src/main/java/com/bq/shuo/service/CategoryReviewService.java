package com.bq.shuo.service;

import com.bq.shuo.model.CategoryReview;
import com.bq.shuo.core.base.BaseService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 分类表  服务实现类
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@Service
@CacheConfig(cacheNames = "categoryReview")
public class CategoryReviewService extends BaseService<CategoryReview> {
	
}