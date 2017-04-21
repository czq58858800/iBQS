package com.bq.shuo.service;

import com.bq.core.Constants;
import com.bq.shuo.mapper.TagMapper;
import com.bq.shuo.model.Tag;
import com.bq.shuo.core.base.BaseService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.cache.annotation.CacheResult;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *   服务实现类
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@Service
public class TagService extends BaseService<Tag> {

    @CacheResult(cacheName ="tag:"+"queryByAll")
    public List<Tag> queryByAll() {
        List<String> records = ((TagMapper) mapper).selectIdPage(Collections.<String, Object>emptyMap());
        return getList(records);
    }
}