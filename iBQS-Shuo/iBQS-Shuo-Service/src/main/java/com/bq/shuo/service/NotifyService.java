package com.bq.shuo.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.Constants;
import com.bq.shuo.mapper.NotifyMapper;
import com.bq.shuo.model.Notify;
import com.bq.shuo.core.base.BaseService;
import com.bq.shuo.model.Subject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;
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
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"notify")
public class NotifyService extends BaseService<Notify> {

    @Autowired
    private NotifyMapper notifyMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private SubjectService subjectService;

    public Page<Notify> queryBeans(Map<String, Object> params) {
        Page<Notify> pageInfo = query(params);
        for (Notify record:pageInfo.getRecords()) {
            if (StringUtils.isNotBlank(record.getSendUserId())) {
                record.setSendUser(userService.queryById(record.getSendUserId()));
            }
            if (StringUtils.isNotBlank(record.getSubjectId())) {
                Subject subject = subjectService.queryById(record.getSubjectId());
                record.setSubject(subject);
            }
        }
        return pageInfo;
    }

    @Override
    public void delete(String id) {
        super.delete(id);
    }

    public void deleteNotify(Notify record) {
        Wrapper<Notify> wrapper = new EntityWrapper(record);
        List<Notify> notifyList = notifyMapper.selectList(wrapper);
        if (notifyList != null && notifyList.size() > 0) {
            for (Notify notify:notifyList) {
                delete(notify.getId());
            }
        }
    }

    @Override
    public Notify update(Notify record) {
        return super.update(record);
    }
}