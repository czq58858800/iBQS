package com.bq.shuo.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.Constants;
import com.bq.core.util.CacheUtil;
import com.bq.core.util.InstanceUtil;
import com.bq.shuo.core.helper.CounterHelper;
import com.bq.shuo.mapper.UserMapper;
import com.bq.shuo.model.SubjectLiked;
import com.bq.shuo.model.User;
import com.bq.shuo.core.base.BaseService;
import com.bq.shuo.model.UserConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表  服务实现类
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@Service
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"user")
public class UserService extends BaseService<User> {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private SubjectLikedService subjectLikedService;

    @Autowired
    private ForwardService forwardService;

    @Autowired
    private UserFollowingService userFollowingService;

    @Autowired
    private CategoryCollectionService categoryCollectionService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserConfigService userConfigService;

    @Autowired
    private BlacklistService blacklistService;

    public Page<User> queryBeans(Map<String, Object> params) {
        Page<String> idPage = this.getPage(params);
        idPage.setRecords(mapper.selectIdPage(idPage, params));
        Page<User> pageInfo = getPage(idPage);
        for (User record:pageInfo.getRecords()) {
            getDetail(record,params);
        }
        return pageInfo;
    }

    public User getDetail(User record,Map<String,Object> params) {
            record = getDetail(record);
        if (params.containsKey("currUserId")) {
            String currUserId = (String) params.get("currUserId");
            record.setFollow(userFollowingService.selectByIsFollow(currUserId,record.getId()));
        }
        return record;
    }

    public User queryBeanById(String userId,String currUserId) {
        User record = queryById(userId);
        if (record != null) {
            getDetail(record);
            if (StringUtils.isNotBlank(currUserId)) {

                // 是否允许私信
                boolean isMessage = false;
                if (!StringUtils.equals(userId, currUserId)) {
                    boolean isBlacklist = blacklistService.selectIsBlacklistByUserId(userId, currUserId);
                    if (!isBlacklist && !record.getConfig().getIsMessage()) {
                        isMessage = userFollowingService.selectByIsFollow(userId, currUserId);
                    } else {
                        isMessage = !isBlacklist;
                    }
                    // currUserId是否关注了userId
                    record.setFollow(userFollowingService.selectByIsFollow(currUserId, userId));
                }
                record.setMessage(isMessage);
            }
        }


        return record;
    }

    public User getDetail(User record) {
        if (record != null && StringUtils.isNotBlank(record.getId())) {
            String id = record.getId();
            UserConfig userConfig = userConfigService.selectByUserId(id);
            record.setConfig(userConfig);
            record.setWorksLikeNum(selectUserCounter(id, CounterHelper.User.WORKS_LIKE));
            record.setWorksNum(selectUserCounter(id, CounterHelper.User.WORKS));
            record.setMyLikeWorksNum(selectUserCounter(id, CounterHelper.User.MY_LIKE_WORKS));
            record.setFollowNum(selectUserCounter(id, CounterHelper.User.FOLLOW));
            record.setFansNum(selectUserCounter(id, CounterHelper.User.FANS));
            record.setForwardNum(selectUserCounter(id, CounterHelper.User.FORWARD));
            return record;
        }
        return record;
    }

    public User selectOne(Map<String, Object> params) {
        String userId = userMapper.selectOne(params);
        if (StringUtils.isNotBlank(userId)) {
            User record = queryById(userId);
            return getDetail(record,params);
        }
        return new User();
    }

    public User queryBeanById(String id) {
        User record = queryById(id);
        return getDetail(record);
    }



    public int selectUserCounter(String userId, String field) {
        String key = CounterHelper.User.USER_COUNTER_KEY + userId;
        if (CacheUtil.getCache().hexists(key,field)) {
            String counter = (String) CacheUtil.getCache().hget(key,field);
            return Integer.parseInt(counter);
        }
        Integer counter = 0;
        if (StringUtils.equals(CounterHelper.User.WORKS,field)) {
            counter = subjectService.selectCountByUserId(userId);
        } else if (StringUtils.equals(CounterHelper.User.WORKS_LIKE,field)) {
            counter = subjectLikedService.selectCountByUserId(userId);
        } else if (StringUtils.equals(CounterHelper.User.MY_LIKE_WORKS,field)) {
            counter = subjectLikedService.selectMyLikedCountByUserId(userId);
        } else if (StringUtils.equals(CounterHelper.User.FORWARD,field)) {
            counter = forwardService.selectCountByUserId(userId);
        } else if (StringUtils.equals(CounterHelper.User.FOLLOW,field)) {
            counter = userFollowingService.selectFollowCountByUserId(userId);
        } else if (StringUtils.equals(CounterHelper.User.FANS,field)) {
            counter = userFollowingService.selectFansCountByUserId(userId);
        } else if (StringUtils.equals(CounterHelper.User.STUFF_COLL,field)) {
            counter = categoryCollectionService.selectCountByUserId(userId);
        } else if (StringUtils.equals(CounterHelper.User.MY_COLL_STUFF,field)) {
            counter = categoryCollectionService.selectMyCollCountByUserId(userId);
        }
        CacheUtil.getCache().hset(key,field,String.valueOf(counter));
        return counter;
    }

    /**
     * 校验帐号或名称是否存在
     * @param params
     * @return boolean
     */
    public boolean selectCheck(Map<String, Object> params) {
        List<String> list = userMapper.selectIdPage(params);
        if (!list.isEmpty()) return true;// 存在用户
        return false;//不存在
    }

    public void incrUserCounter(String userId, String field) {
        setUserCounter(userId,field,+1);
    }

    public void decrUserCounter(String userId, String field) {
        setUserCounter(userId,field,-1);
    }

    public void setUserCounter(String userId, String field,int cal) {
        if (StringUtils.isNotBlank(userId)) {
            String key = CounterHelper.User.USER_COUNTER_KEY + userId;

            String lockKey = new StringBuilder(Constants.CACHE_NAMESPACE).append(CounterHelper.User.USER_COUNTER_KEY).append("LOCK:").append(userId).toString();

            while (!CacheUtil.getLock(lockKey)) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    logger.error("", e);
                }
            }

            try {
                Integer number = selectUserCounter(userId, field)+cal;
                if (number < 0) {
                    number = selectUserCounter(userId,field);
                }
                logger.info("用户（User）"+field+":" + number);
                CacheUtil.getCache().hset(key,field,String.valueOf(number));
                userMapper.updateCounter(userId,field,number);

            } finally {
                CacheUtil.unlock(lockKey);
            }
        }
    }


    public void setCounterNum(String id, String field, Integer counter) {
        String key = CounterHelper.User.USER_COUNTER_KEY + id;
        CacheUtil.getCache().hset(key,field,String.valueOf(counter));
    }

    public void init() {
        List<String> list = ((UserMapper) mapper).selectIdPage(Collections.<String, Object>emptyMap());
        for (String id : list) {
            CacheUtil.getCache().set(getCacheKey(id), mapper.selectById(id));
        }
    }

    public User selectByName(String userName) {
        Map<String,Object> params = InstanceUtil.newHashMap();
        params.put("name",userName);
        Page<User> page = queryBeans(params);
        if (page != null && page.getRecords().size() > 0) {
            return page.getRecords().get(0);
        }
        return new User();
    }

    public User selectByPhone(String phone) {
        Map<String,Object> params = InstanceUtil.newHashMap();
        params.put("phone",phone);
        Page<User> page = queryBeans(params);
        if (page != null && page.getRecords() != null && page.getRecords().size() > 0) {
            return page.getRecords().get(0);
        }
        return null;
    }

    public User selectByAccount(String account) {
        Map<String,Object> params = InstanceUtil.newHashMap();
        params.put("account",account);
        Page<User> page = query(params);
        if (page != null && page.getRecords() != null && page.getRecords().size() > 0) {
            return page.getRecords().get(0);
        }
        return null;
    }

    public String queryAccountByToken(String token) {
        Map<String,Object> params = InstanceUtil.newHashMap();
        params.put("token",token);
        Page<User> page = query(params);
        if (page != null && page.getRecords() != null && page.getRecords().size() > 0) {
            return page.getRecords().get(0).getAccount();
        }
        return null;
    }

    @Override
    public User update(User record) {
        if (StringUtils.isBlank(record.getId())) {
            record = super.update(record);
            userConfigService.update(new UserConfig(record.getId()));
            return record;
        } else {
            return super.update(record);
        }

    }
}