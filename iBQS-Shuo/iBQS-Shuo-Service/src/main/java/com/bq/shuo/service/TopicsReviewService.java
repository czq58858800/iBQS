package com.bq.shuo.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.Constants;
import com.bq.core.util.InstanceUtil;
import com.bq.shuo.core.base.BaseService;
import com.bq.shuo.mapper.TopicsReviewMapper;
import com.bq.shuo.model.TopicsReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 话题表  服务实现类
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@Service
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"topicsReview")
public class TopicsReviewService extends BaseService<TopicsReview> {

    @Autowired
    private TopicsReviewMapper topicsReviewMapper;

    @Autowired
    private TopicsService topicsService;

    @Autowired
    private UserService userService;

    public Page<TopicsReview> queryGroupTopic(Map<String,Object> params) {
        Page<String> idPage = getPage(params);
        idPage.setRecords(topicsReviewMapper.selectIdByGroupTopic(idPage, params));
        if (idPage != null) {
            Page<TopicsReview> page = new Page<TopicsReview>(idPage.getCurrent(), idPage.getSize());
            page.setTotal(idPage.getTotal());
            List<TopicsReview> records = InstanceUtil.newArrayList();
            for (String id : idPage.getRecords()) {
                TopicsReview record = this.queryById(id);
                record.setTopics(topicsService.queryById(record.getTopicId()));
                record.setOwner(userService.queryById(record.getOwnerId()));
                record.getOwner().setToken(null);
                record.getOwner().setPassword(null);
                records.add(record);
            }
            page.setRecords(records);
            return page;
        }
        return new Page<TopicsReview>();
    }
	
}