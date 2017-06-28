package com.bq.shuo.service;

import com.bq.core.Constants;
import com.bq.shuo.core.base.BaseService;
import com.bq.shuo.mapper.SearchHotMapper;
import com.bq.shuo.model.SearchHot;
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
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"searchHot")
public class SearchHotService extends BaseService<SearchHot> {

    @Autowired
    private SearchHotMapper searchHotMapper;

    public SearchHot updateByKeyword(Integer type,String keyword) {
        SearchHot record = selectByKeyword(keyword);
        if (record != null) {
            record.setSearchNum(record.getSearchNum()+1);
        } else {
            record = new SearchHot();
            record.setText(keyword);
            record.setSearchNum(1);
        }
        record.setType(type);
        return super.update(record);
    }


    public SearchHot selectByKeyword(String keyword) {
        String id = searchHotMapper.selectByKeyword(keyword);
        if (StringUtils.isNotBlank(id)) {
            return queryById(id);
        }
        return null;
    }
}