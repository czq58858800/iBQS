package com.bq.shuo.service;

import com.bq.core.Constants;
import com.bq.shuo.core.base.BaseService;
import com.bq.shuo.mapper.UserConfigMapper;
import com.bq.shuo.model.UserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用戶配置表  服务实现类
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@Service
public class UserConfigService extends BaseService<UserConfig>  {
    @Autowired
    private UserConfigMapper userConfigMapper;

    @Cacheable("userConfig")
    public UserConfig selectByUserId(String userId) {
        UserConfig record = queryById(userConfigMapper.selectByUserId(userId));
        return record;
    }

    @Override
    @CachePut("userConfig")
    public UserConfig update(UserConfig record) {
        return super.update(record);
    }
}
