package com.bq.shuo.service;

import com.bq.core.Constants;
import com.bq.shuo.core.base.BaseService;
import com.bq.shuo.core.support.login.ThirdPartyUser;
import com.bq.shuo.mapper.UserThirdpartyMapper;
import com.bq.shuo.model.User;
import com.bq.shuo.model.UserThirdparty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 第三方用户  服务实现类
 * </p>
 *
 * @author chern.zq
 * @since 2017-04-13
 */
@Service
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"userThirdparty")
public class UserThirdpartyService extends BaseService<UserThirdparty> {


    @Autowired
    private UserThirdpartyMapper thirdpartyMapper;

    @Autowired
    private UserService userService;



    /**
     * 查询第三方帐号
     */
    public UserThirdparty queryByThirdParty(String openId, String provider) {
        String thirdpartyId = thirdpartyMapper.queryIdByThireParty(provider,openId);
        if (StringUtils.isNotBlank(thirdpartyId)) {
            return queryById(thirdpartyId);
        }
        return null;
    }

    @Transactional
    public User insertThirdPartyUser(ThirdPartyUser thirdPartyUser) {
        User user = new User();
        user.setSex(Integer.parseInt(thirdPartyUser.getGender()));
        user.setUserType(thirdPartyUser.getVerified() ? "2":"1");
        user.setName(thirdPartyUser.getUserName());
        user.setAvatar(thirdPartyUser.getAvatarUrl());
        user.setAddress(thirdPartyUser.getLocation());
        user.setSummary(thirdPartyUser.getDescription());
        user.setLocked(false);
        user.setRcmd(false);
        // 初始化第三方信息
        UserThirdparty thirdparty = new UserThirdparty();
        thirdparty.setProvider(thirdPartyUser.getProvider());
        thirdparty.setOpenId(thirdPartyUser.getOpenid());
        thirdparty.setCreateTime(new Date());
        thirdparty.setUpdateTime(new Date());
        thirdparty.setName(thirdPartyUser.getThirdpartyName());
        thirdparty.setAvatar(thirdPartyUser.getAvatarUrl());
        thirdparty.setToken(thirdPartyUser.getToken());
        thirdparty.setVerified(thirdPartyUser.getVerified());
        thirdparty.setVerifiedReason(thirdPartyUser.getVerifiedReason());

        userService.update(user);

        thirdparty.setUserId(user.getId());

        update(thirdparty);

        user.setAccount(thirdparty.getProvider() + user.getId());

        userService.update(user);

        return  user;
    }

    @Cacheable
    public List<UserThirdparty> queryThirdPartByList(String userId) {
        List<String> ids = thirdpartyMapper.selectIdByList(userId);
        List<UserThirdparty> userThirdpartyList = getList(ids);
        return userThirdpartyList;
    }

    public List<UserThirdparty> selectThirdPartByList(String userId) {
        List<String> ids = thirdpartyMapper.selectIdByList(userId);
        List<UserThirdparty> userThirdpartyList = getList(ids);
        return userThirdpartyList;
    }

    @Cacheable
    public UserThirdparty queryThirdPartyByUserId(String userId, String thirdProvider) {
        return queryById(thirdpartyMapper.queryThirdPartyByUserId(userId,thirdProvider));
    }

    public UserThirdparty queryBeanById(String id) {
        UserThirdparty record = queryById(id);
        record.setUser(userService.queryById(record.getId()));
        return record;
    }

    @Override
    public UserThirdparty queryById(String id) {
        UserThirdparty record = super.queryById(id);
        record.setUser(userService.queryById(record.getId()));
        return record;
    }

    public int selectCountByUserId(String userId) {
        return thirdpartyMapper.selectCountByUserId(userId);
    }
	
}