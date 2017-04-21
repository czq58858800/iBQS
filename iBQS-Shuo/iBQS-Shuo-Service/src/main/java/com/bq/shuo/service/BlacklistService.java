package com.bq.shuo.service;

import com.bq.shuo.mapper.BlacklistMapper;
import com.bq.shuo.model.Blacklist;
import com.bq.shuo.core.base.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * <p>
 *   服务实现类
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@Service
@CacheConfig(cacheNames = "blacklist")
public class BlacklistService extends BaseService<Blacklist> {

    @Autowired
    private BlacklistMapper blacklistMapper;

    public boolean selectIsBlacklistByUserId(String userId, String blacklistUserId) {
        if (StringUtils.isNotBlank(selectIdByUserId(userId,blacklistUserId))) {
            return true;
        }
        return false;
    }

    public String selectIdByUserId(String userId, String blacklistUserId) {
        return blacklistMapper.selectIsBlacklistByUserId(userId,blacklistUserId);
    }
	
}