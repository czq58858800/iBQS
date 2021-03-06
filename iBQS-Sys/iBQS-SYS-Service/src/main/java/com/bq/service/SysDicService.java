package com.bq.service;

import java.util.List;
import java.util.Map;

import com.bq.core.util.InstanceUtil;
import com.bq.core.Constants;
import com.bq.core.base.BaseService;
import com.bq.model.SysDic;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author chern.zq
 * @version 2016年5月20日 下午3:19:19
 */
@Service
@CacheConfig(cacheNames = "sysDic")
public class SysDicService extends BaseService<SysDic> {

    @Cacheable(value = Constants.CACHE_NAMESPACE + "sysDics")
    public Map<String, Map<String, String>> getAllDic() {
        Map<String, Object> params = InstanceUtil.newHashMap();
        params.put("orderBy", "sort_no");
        List<SysDic> list = queryList(params);
        Map<String, Map<String, String>> resultMap = InstanceUtil.newHashMap();
        for (SysDic sysDic : list) {
            String key = sysDic.getType();
            if (resultMap.get(key) == null) {
                Map<String, String> dicMap = InstanceUtil.newHashMap();
                resultMap.put(key, dicMap);
            }
            resultMap.get(key).put(sysDic.getCode(), sysDic.getCodeText());
        }
        return resultMap;
    }

    public Map<String, String> queryDicByType(String key) {
        return applicationContext.getBean(SysDicService.class).getAllDic().get(key);
    }
}
