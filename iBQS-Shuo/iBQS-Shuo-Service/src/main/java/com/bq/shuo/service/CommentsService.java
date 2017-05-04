package com.bq.shuo.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.Constants;
import com.bq.core.util.CacheUtil;
import com.bq.shuo.core.helper.CounterHelper;
import com.bq.shuo.core.helper.PushType;
import com.bq.shuo.mapper.CommentsMapper;
import com.bq.shuo.mapper.CommentsPraiseMapper;
import com.bq.shuo.model.Comments;
import com.bq.shuo.core.base.BaseService;
import com.bq.shuo.model.Notify;
import io.rong.methods.Push;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.Map;

/**
 * <p>
 * 评论表  服务实现类
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@Service
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"comments")
public class CommentsService extends BaseService<Comments> {
    @Autowired
    private CommentsMapper commentsMapper;

    @Autowired
    private CommentsPraiseService commentsPraiseService;

    @Autowired
    private UserService userService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private NotifyService notifyService;

    public Page<Comments> queryBeans(Map<String, Object> params) {
        Page<Comments> pageInfo = query(params);
        for (Comments record:pageInfo.getRecords()) {
            record.setPraiseNum(selectCommentsCounter(record.getId(),"praise_num"));
            if (StringUtils.isNotBlank(record.getUserId())) {
                record.setReplyUser(userService.queryById(record.getUserId()));
            }

            if (StringUtils.isNotBlank(record.getBeReplyId())) {
                record.setBeReplyUser(userService.queryById(record.getBeReplyId()));
            }

            if (params.containsKey("currUserId")) {
                String currUserId = (String) params.get("currUserId");
                record.setPraise(commentsPraiseService.selectByIsPraise(record.getId(),currUserId));
            }

            if (StringUtils.isNotBlank(record.getBeReplyCommentId())) {
                record.setBeReplyComment(this.queryById(record.getBeReplyCommentId()));
            }
        }
        return pageInfo;
    }

    @Override
    public Comments update(Comments record) {
        if (StringUtils.isBlank(record.getId())) {
            subjectService.incrSubjectCounter(record.getSubjectId(),CounterHelper.Subject.COMMENTS);
        }
        record = super.update(record);
        return record;
    }

    @Transactional
    public void delete(String id,String currUserId) {
        Comments record = queryById(id);
        if (record != null) {
            subjectService.decrSubjectCounter(record.getSubjectId(), CounterHelper.Subject.COMMENTS);
            commentsMapper.deleteCounter(id);
            commentsMapper.deleteByCurrUserId(currUserId,id);
            CacheUtil.getCache().del(getCacheKey(id));

            Notify notify = new Notify(currUserId,record.getSubjectId(),record.getId());
            notifyService.delete(notify);
            notify.setMsgType(PushType.AT);
            notifyService.delete(notify);
        }
    }

    @Transactional
    public void insertCommentPraise(String commentId, String userId) {
        incrCommentsCounter(commentId,CounterHelper.Comments.PRAISE);
        commentsMapper.insertCommentPraise(commentId,userId);
    }

    public int selectCountBySubjectId(String subjectId) {
        return commentsMapper.selectCountBySubjectId(subjectId);
    }


    public Integer selectCommentsCounter(String commentsId,String field) {
        String key =  CounterHelper.Comments.COMMENTS_COUNTER_KEY+ commentsId;

        if (CacheUtil.getCache().exists(key)) {
            String counter = (String) CacheUtil.getCache().hget(key,field);
            return Integer.parseInt(counter);
        }
        int counter = 0;
        if(StringUtils.equals(CounterHelper.Comments.PRAISE,field)) {
            counter = commentsMapper.selectCommentIdByPraiseCount(commentsId);
        }
        CacheUtil.getCache().hset(key,field,String.valueOf(counter));
        return counter;
    }

    public void incrCommentsCounter(String commentsId, String field) {
        setCommentsCounter(commentsId,field,+1);
    }

    public void decrCommentsCounter(String commentsId, String field) {
        setCommentsCounter(commentsId,field,-1);
    }


    public void setCommentsCounter(String commentsId, String field,int cal) {
        if (StringUtils.isNotBlank(commentsId)) {
            String key =  CounterHelper.Comments.COMMENTS_COUNTER_KEY+ commentsId;

            while (!CacheUtil.getLock(key)) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    logger.error("", e);
                }
            }

            try {
                Integer number = selectCommentsCounter(commentsId, field)+cal;
                logger.info("评论（Comments）"+field+":" + number);
                CacheUtil.getCache().hset(key,field,String.valueOf(number));
                commentsMapper.updateCounter(commentsId,field,number);
            } finally {
                CacheUtil.unlock(key);
            }
        }
    }
}