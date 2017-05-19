package com.bq.shuo.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.Constants;
import com.bq.core.util.InstanceUtil;
import com.bq.shuo.mapper.TopicsMapper;
import com.bq.shuo.model.Subject;
import com.bq.shuo.model.Topics;
import com.bq.shuo.core.base.BaseService;
import com.bq.shuo.model.User;
import org.apache.commons.lang3.StringUtils;
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
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"topics")
public class TopicsService extends BaseService<Topics> {

    @Autowired
    private TopicsMapper topicsMapper;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserFollowingService userFollowingService;

    public Page<Topics> queryBeans(Map<String, Object> params) {
        Page<Topics> page = query(params);
        String currUserId = params.containsKey("currUserId") ? (String) params.get("currUserId") : null;
        for (Topics record:page.getRecords()) {
            getDetail(record, currUserId);
        }
        return page;
    }

    public List<Topics> queryByHot(Map<String,Object> params) {

        List<Topics> topics = getList(topicsMapper.queryByHot(params));
        String currUserId = (String) params.get("currUserId");
        for (Topics topic:topics) {
            getDetail(topic,currUserId);
        }
        return topics;
    }

    public Topics queryBeansByKeyword(String keyword, String currUserId) {
        Topics topics = queryById(topicsMapper.queryBeanByKeyword(keyword));
        if (topics != null && StringUtils.isNotBlank(topics.getId())) {
            Topics record = InstanceUtil.to(topics, Topics.class);
            return getDetail(record, currUserId);
        }
        return null;
    }

    public Topics getDetail(Topics record, String currUserId) {
        if (StringUtils.isBlank(record.getCover())) {
            Map<String,Object> params = InstanceUtil.newHashMap();
            params.put("keyword",record.getName());
            params.put("topicOrderHot",true);
            params.put("pageSize",1);
//            Page<Subject> subjectPage = subjectService.queryByHot(params);
//            if (subjectPage != null && subjectPage.getRecords() != null && subjectPage.getRecords().size() > 0) {
//                Subject subject = subjectPage.getRecords().get(0);
//                record.setCover(subject.getCover());
//                record.setCoverType(subject.getCoverType());
//                record.setCoverWidth(subject.getCoverWidth());
//                record.setCoverHeight(subject.getCoverHeight());
//            }
        }
        if (record.getOwnerId() != null) {
            User owner = userService.queryById(record.getOwnerId());
            if (owner != null) {
                owner.setFollow(userFollowingService.selectByIsFollow(currUserId,owner.getId()));
            }
            record.setOwner(owner);
        }
        return record;
    }

    public String selectIdByName(String name) {
        return topicsMapper.selectIdByName(name);
    }
}