package com.bq.shuo.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.Constants;
import com.bq.core.util.CacheUtil;
import com.bq.shuo.core.helper.CounterHelper;
import com.bq.shuo.mapper.UserFollowingMapper;
import com.bq.shuo.model.User;
import com.bq.shuo.model.UserFollowing;
import com.bq.shuo.core.base.BaseService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户关注表  服务实现类
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@Service
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"userFollowing")
public class UserFollowingService extends BaseService<UserFollowing> {

    @Autowired
    private UserFollowingMapper userFollowingMapper;

    @Autowired
    private UserService userService;

    public Page<User> queryByUserBeans(Map<String, Object> params) {
        Page<String> idPage = this.getPage(params);
        idPage.setRecords(userFollowingMapper.selectUserIdByMap(idPage,params));
        Page<User> pageInfo = userService.getPage(idPage);
        for (User record:pageInfo.getRecords()) {
            userService.getDetail(record,params);
        }
        return pageInfo;
    }

    public boolean selectByIsFollow(String userId, String beUserId) {
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(beUserId)) {
            String key = Constants.CACHE_RELATIONS_USER + userId;
            if (CacheUtil.getCache().hexists(key, beUserId)) {
                System.out.println(BooleanUtils.toBoolean((String) CacheUtil.getCache().hget(key, beUserId)));
                return BooleanUtils.toBoolean((String) CacheUtil.getCache().hget(key, beUserId));
            } else {
                boolean isLiked = false;
                if (StringUtils.isNotBlank(selectByFollowId(userId, beUserId))) {
                    isLiked = true;
                }
                CacheUtil.getCache().hset(key, beUserId, BooleanUtils.toStringTrueFalse(isLiked));
                CacheUtil.getCache().expire(key,600);
                return isLiked;
            }
        }
        return false;
    }

    public List<String> selectByUserIdFollows(String userId) {
        return userFollowingMapper.selectByUserIdFollows(userId);
    }

    public Integer selectFollowCountByUserId(String userId) {
        return userFollowingMapper.selectFollowCountByUserId(userId);
    }

    public Integer selectFansCountByUserId(String userId) {
        return userFollowingMapper.selectFansCountByUserId(userId);
    }

    public synchronized boolean updateFollow(String userId, String beUserId) {
        String id = selectByFollowId(userId,beUserId);
        if (StringUtils.isBlank(id)) {
            update(new UserFollowing(userId,beUserId));

            // 添加关注数计数器
            userService.incrUserCounter(userId, CounterHelper.User.FOLLOW);
            // 添加粉丝数计数器
            userService.incrUserCounter(beUserId,CounterHelper.User.FANS);

            String key = Constants.CACHE_RELATIONS_USER + userId;
            CacheUtil.getCache().hset(key, beUserId, BooleanUtils.toStringTrueFalse(true));
            CacheUtil.getCache().expire(key,600);
            return true;
        }
        return false;
    }

    public synchronized boolean updateFollowCancel(String userId, String beUserId) {
        String id = selectByFollowId(userId,beUserId);
        if (StringUtils.isNotBlank(id)) {
            delete(id);
            // 减去关注数计数器
            userService.decrUserCounter(userId,CounterHelper.User.FOLLOW);
            // 减去粉丝数计数器
            userService.decrUserCounter(beUserId,CounterHelper.User.FANS);

            String key = Constants.CACHE_RELATIONS_USER + userId;
            CacheUtil.getCache().hset(key, beUserId, BooleanUtils.toStringTrueFalse(false));
            CacheUtil.getCache().expire(key,600);
            return true;
        }
        return false;
    }

    public String selectByFollowId(String userId,String beUserId) {
        return userFollowingMapper.selectByFollowId(userId,beUserId);
    }
}