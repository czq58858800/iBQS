package com.bq.shuo.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.Constants;
import com.bq.core.util.InstanceUtil;
import com.bq.shuo.mapper.UserContactsMapper;
import com.bq.shuo.model.User;
import com.bq.shuo.model.UserContacts;
import com.bq.shuo.core.base.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
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
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"userContacts")
public class UserContactsService extends BaseService<UserContacts> {

    @Autowired
    private UserFollowingService userFollowingService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserContactsMapper userContactsMapper;

    @Override
    public UserContacts update(UserContacts record) {
        User user = userService.selectByPhone(record.getPhone());
        if (user != null) {
            record.setContactUserId(user.getId());
        }
        return super.update(record);
    }

    public UserContacts selectByPhone(String deviceId, String userId, String phone) {
        String id = userContactsMapper.selectByPhone(deviceId,userId,phone);
        UserContacts record = null;
        if (StringUtils.isNotBlank(id)) {
            record = queryById(id);
        }
        return record;
    }

    public Page<UserContacts> queryBeans(Map<String, Object> params) {
        Page<String> idPage = this.getPage(params);
        idPage.setRecords(mapper.selectIdPage(idPage, params));
        Page<UserContacts> page = getPage(idPage);
        return page;
    }

    public Page<UserContacts> queryFollow(Map<String,Object> params) {
        Page<String> idPage = this.getPage(params);
        idPage.setRecords(userContactsMapper.selectIdPageByFollow(idPage, params));
        Page<UserContacts> page = getPage(idPage);
        return page;
    }

    @Override
    public Page<UserContacts> getPage(Page<String> ids) {
        if (ids != null) {
            Page<UserContacts> page = new Page(ids.getCurrent(), ids.getSize());
            page.setTotal(ids.getTotal());
            List<UserContacts> records = InstanceUtil.newArrayList();

            for (String id : ids.getRecords()) {
                UserContacts record = this.queryById(id);
                if (StringUtils.isNotBlank(record.getContactUserId())) {
                    User user = userService.queryById(record.getContactUserId());
                    if (user != null) {
                        boolean isFollow = userFollowingService.selectByIsFollow(record.getUserId(), user.getId());
                        user.setFollow(isFollow);
                    }
                    record.setUser(user);

                }
                records.add(record);
            }
            page.setRecords(records);
            return page;
        }
        return new Page();
    }
}