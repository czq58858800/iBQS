package com.bq.shuo.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.Constants;
import com.bq.core.util.InstanceUtil;
import com.bq.shuo.model.Fonts;
import com.bq.shuo.core.base.BaseService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 *   服务实现类
 * </p>
 *
 * @author chern.zq
 * @since 2017-04-13
 */
@Service
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"fonts")
public class FontsService extends BaseService<Fonts> {

    public Fonts getFontByCode(String code) {
        Map<String,Object> params = InstanceUtil.newHashMap();
        params.put("enable",true);
        params.put("code",code);
        Page<Fonts> page = query(params);
        if (page.getTotal() > 0 && page.getRecords().size() > 0) {
            return page.getRecords().get(0);
        }
        return null;
    }
	
}