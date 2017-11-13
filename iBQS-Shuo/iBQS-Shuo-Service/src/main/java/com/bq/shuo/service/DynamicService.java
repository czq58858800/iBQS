package com.bq.shuo.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.Constants;
import com.bq.shuo.mapper.DynamicMapper;
import com.bq.shuo.model.Dynamic;
import com.bq.shuo.core.base.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"dynamic")
public class DynamicService extends BaseService<Dynamic> {

    @Autowired
    private DynamicMapper dynamicMapper;

    @Autowired
    private ForwardService forwardServicep;
    @Autowired
    private SubjectService subjectService;

    public Page<Dynamic> queryBeans(Map<String, Object> params) {
        Page<Dynamic> page = query(params);
        for (Dynamic record : page.getRecords()) {
            String type = record.getType();
            if (StringUtils.equals(type, "2")) { // 转发
                record.setForward(forwardServicep.queryBeanById(record.getValId(),(String) params.get("currUserId")));
            } else if (StringUtils.equals(type,"1")) { // 表情
                record.setSubject(subjectService.queryBeanById(record.getValId(), (String) params.get("currUserId")));
            }
        }
        return page;
    }

    @Transactional
    public void deleteByValId(String valId,String type) {
        String id = dynamicMapper.selectValIdById(valId,type);
        if(StringUtils.isNotBlank(id)) {
            delete(id);
        }

    }
	
}