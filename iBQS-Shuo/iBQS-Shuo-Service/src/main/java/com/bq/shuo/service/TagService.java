package com.bq.shuo.service;

import com.alibaba.dubbo.common.json.JSONObject;
import com.bq.core.Constants;
import com.bq.core.util.InstanceUtil;
import com.bq.shuo.mapper.TagMapper;
import com.bq.shuo.model.Tag;
import com.bq.shuo.core.base.BaseService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.cache.annotation.CacheResult;
import java.util.Collections;
import java.util.List;
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
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"tag")
public class TagService extends BaseService<Tag> {

    @CacheResult(cacheName = Constants.CACHE_NAMESPACE+Constants.CACHE_SHUO_NAMESPACE+"tag:"+"queryByAll")
    public List<Tag> queryByAll() {
        Map<String,Object> params = InstanceUtil.newHashMap();
        params.put("enable",true);
        List<String> records = ((TagMapper) mapper).selectIdPage(params);
        return getList(records);
    }
}