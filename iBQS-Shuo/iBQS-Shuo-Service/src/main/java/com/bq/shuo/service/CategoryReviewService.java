package com.bq.shuo.service;

import com.bq.core.Constants;
import com.bq.shuo.core.base.BaseService;
import com.bq.shuo.mapper.CategoryReviewMapper;
import com.bq.shuo.model.CategoryReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 分类表  服务实现类
 * </p>
 *
 * @author chern.zq
 * @since 2017-04-13
 */
@Service
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"categoryReview")
public class CategoryReviewService extends BaseService<CategoryReview> {

    @Autowired
    private CategoryReviewMapper categoryReviewMapper;


}