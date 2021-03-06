package com.bq.shuo.service;

import com.bq.core.Constants;
import com.bq.core.util.PropertiesUtil;
import com.bq.shuo.mapper.BlacklistMapper;
import com.bq.shuo.model.Blacklist;
import com.bq.shuo.core.base.BaseService;
import io.rong.RongCloud;
import io.rong.models.CodeSuccessReslut;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * <p>
 *   服务实现类
 * </p>
 *
 * @author chern.zq
 * @since 2017-04-13
 */
@Service
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"blacklist")
public class BlacklistService extends BaseService<Blacklist> {

    @Autowired
    private BlacklistMapper blacklistMapper;

    // 融云
    private final static String appKey = PropertiesUtil.getString("rong.access_key");
    private final static String appSecret = PropertiesUtil.getString("rong.secret_key");


    @Override
    public Blacklist update(Blacklist record) {
        RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret);
        try {
            // 加入黑名单
            CodeSuccessReslut userAddBlacklistResult = rongCloud.user.addBlacklist(record.getUserId(), record.getBlacklistUserId());
            logger.debug("refresh:  {}",userAddBlacklistResult.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.update(record);
    }

    @Override
    public void delete(String id) {
        RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret);
        try {
            Blacklist record = super.queryById(id);
            if (record != null) {
                // 移出黑名单
                CodeSuccessReslut userRemoveBlacklistResult = rongCloud.user.removeBlacklist(record.getUserId(), record.getBlacklistUserId());
                logger.debug("refresh:  {}",userRemoveBlacklistResult.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.delete(id);
    }

    /**
     * 判断是否在黑名单
     * @param userId 用户ID
     * @param blacklistUserId 查询用户ID
     * @return 是否在黑名单
     */
    public boolean selectIsBlacklistByUserId(String userId, String blacklistUserId) {
        if (StringUtils.isNotBlank(selectIdByUserId(userId,blacklistUserId))) {
            return true;
        }
        return false;
    }

    /**
     * 获取黑名单ID
     * @param userId 用户ID
     * @param blacklistUserId 查询用户ID
     * @return 黑名单ID
     */
    public String selectIdByUserId(String userId, String blacklistUserId) {
        return blacklistMapper.selectIsBlacklistByUserId(userId,blacklistUserId);
    }
	
}