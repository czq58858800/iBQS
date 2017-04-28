package com.bq.shuo.service;

import com.bq.core.Constants;
import com.bq.core.util.CacheUtil;
import com.bq.core.util.InstanceUtil;
import com.bq.shuo.core.base.BaseService;
import com.bq.shuo.mapper.SystemConfigMapper;
import com.bq.shuo.model.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系統配置表  服务实现类
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@Service
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"systemConfig")
public class SystemConfigService extends BaseService<SystemConfig> {

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    public void getAll() {
        if (!CacheUtil.getCache().exists(Constants.SYSTEM_CONFIG_CACHA)) {
            Map<String, Object> params = InstanceUtil.newHashMap();
            params.put("enable", 1);
            List<String> idList = systemConfigMapper.selectIdPage(params);
            for (String id : idList) {
                SystemConfig record = queryById(id);
                CacheUtil.getCache().hset(Constants.SYSTEM_CONFIG_CACHA, record.getCode(), record.getValue());
            }
        }
    }


    public void init() {
        getAll();
    }
}
