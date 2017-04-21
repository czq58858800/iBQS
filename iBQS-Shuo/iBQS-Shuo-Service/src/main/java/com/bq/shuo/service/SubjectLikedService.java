package com.bq.shuo.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.bq.shuo.core.helper.CounterHelper;
import com.bq.shuo.core.util.SystemConfigUtil;
import com.bq.shuo.mapper.SubjectLikedMapper;
import com.bq.shuo.model.Subject;
import com.bq.shuo.model.SubjectLiked;
import com.bq.shuo.core.base.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 主题关注表  服务实现类
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@Service
public class SubjectLikedService extends BaseService<SubjectLiked> {

    @Autowired
    private SubjectLikedMapper subjectLikedMapper;

    @Autowired
    private SubjectService subjectService;
    @Autowired
    private UserService userService;

    public Page<SubjectLiked> queryBeans(Map<String, Object> params) {
        Page<SubjectLiked> page = query(params);
        String currUserId = (String) params.get("currUserId");
        for (SubjectLiked record:page.getRecords()) {
            record.setSubject(subjectService.queryBeanById(record.getSubjectId(),currUserId));
            record.setUser(userService.queryById(record.getUserId()));
        }
        return page;
    }

    public boolean selectByIsLiked(String subjectId, String userId) {
        if (StringUtils.isNotBlank(selectLikedById(subjectId,userId))) {
            return true;
        }
        return false;
    }

    public String selectLikedById(String subjectId, String userId) {
        return subjectLikedMapper.selectLikedById(subjectId,userId);
    }

    public synchronized boolean updateLiked(String subjectId, String userId) {
        if (StringUtils.isBlank(selectLikedById(subjectId,userId))) {
            Subject subject = subjectService.queryById(subjectId);
            if (subject!=null) {
                update(new SubjectLiked(userId,subjectId));
                // 主题喜欢数+1
                subjectService.incrSubjectCounter(subjectId, CounterHelper.Subject.LIKED);
                // 用户喜欢作品数+1
                userService.incrUserCounter(subject.getUserId(),CounterHelper.Subject.WORKS_LIKE);
                // 作者作品喜欢数+1
                userService.incrUserCounter(userId,CounterHelper.Subject.MY_LIKE_WORKS);

                int likedCount = selectCountBySubjectId(subjectId);

                int maxCount = SystemConfigUtil.getIntValue(SystemConfigUtil.SUBJECT_LIKED_HOT_NUM);
                if ( likedCount == maxCount) {
                    subject.setIsHot(true);
                    subject.setHotTime(new Date());
                    subjectService.update(subject);
                }
                return true;
            }
        }

        return false;
    }

    public synchronized boolean updateCancelLiked(String subjectId, String userId) {
        String followId = selectLikedById(subjectId,userId);
        if (StringUtils.isNotBlank(followId)) {
            Subject subject = subjectService.queryById(subjectId);
            if (subject!=null) {
                delete(followId);
                // 主题喜欢数-1
                subjectService.decrSubjectCounter(subjectId,CounterHelper.Subject.LIKED);
                // 用户喜欢作品数-1
                userService.decrUserCounter(subject.getUserId(),CounterHelper.Subject.WORKS_LIKE);
                // 作者作品喜欢数-1
                userService.decrUserCounter(userId,CounterHelper.Subject.MY_LIKE_WORKS);
                return true;
            }
        }
        return false;
    }

    public int selectCountBySubjectId(String subjectId) {
        return subjectLikedMapper.selectCountBySubjectId(subjectId);
    }

    public Integer selectCountByUserId(String userId) {
        return subjectLikedMapper.selectCountByUserId(userId);
    }

    public Integer selectMyLikedCountByUserId(String userId) {
        return subjectLikedMapper.selectMyLikedCountByUserId(userId);
    }
}