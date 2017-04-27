package com.bq.shuo.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.bq.shuo.core.helper.CounterHelper;
import com.bq.shuo.mapper.ForwardMapper;
import com.bq.shuo.model.Forward;
import com.bq.shuo.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 *   服务实现类
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@Service
@CacheConfig(cacheNames = "forward")
public class ForwardService extends BaseService<Forward> {

    @Autowired
    private ForwardMapper forwardMapper;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private UserService userService;

    @Autowired
    private DynamicService dynamicService;

    public int selectCountBySubjectId(String subjectId) {
        return forwardMapper.selectCountBySubjectId(subjectId);
    }

    public int selectCountByUserId(String userId) {
        return forwardMapper.selectCountByUserId(userId);
    }

    public Page<Forward> queryBeans(Map<String,Object> params) {
        Page<Forward> page = query(params);
        for (Forward record:page.getRecords()) {
            record.setUser(userService.queryById(record.getUserId()));
            String userId = null;
            if (params.containsKey("currUserId")) {
                userId = (String) params.get("currUserId");
            }

            record.setSubject(subjectService.queryBeanById(record.getSubjectId(), userId));
        }
        return page;
    }

    public Forward queryBeanById(String id, String currUserId) {
        Forward record = queryById(id);
        if (record != null) {
            record.setSubject(subjectService.queryBeanById(record.getSubjectId(), currUserId));
            record.setUser(userService.queryById(record.getUserId()));
        }
        return record;
    }

    public void deleteBySubjectId(String id, String userId, Integer forwardNum) {
        userService.setCounterNum(userId, "forward_num",forwardNum);
        EntityWrapper<Forward> wrapper = new EntityWrapper<>();
        Forward record = new Forward();
        record.setSubjectId(id);
        wrapper.setEntity(record);
        mapper.delete(wrapper);
        dynamicService.deleteByValId(id,"2");
    }

    @Override
    public Forward update(Forward record) {
        record = super.update(record);
        userService.incrUserCounter(record.getUserId(), CounterHelper.User.FORWARD);
        subjectService.incrSubjectCounter(record.getSubjectId(),CounterHelper.Subject.FORWARD);
        return record;
    }

    @Override
    public void delete(String id) {
        Forward record = queryById(id);
        if (record != null) {
            //Redis转发数计数器
            subjectService.decrSubjectCounter(record.getSubjectId(), CounterHelper.Subject.FORWARD);
            //Redis用户转发数
            userService.decrUserCounter(record.getUserId(), CounterHelper.User.FORWARD);
            dynamicService.deleteByValId(id,"2");
            super.delete(id);

        }

    }
}