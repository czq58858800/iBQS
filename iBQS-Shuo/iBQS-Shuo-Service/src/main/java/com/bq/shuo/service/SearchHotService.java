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
 *   搜索热词服务实现类
 * </p>
 *
 * @author chern.zq
 * @since 2017-04-13
 */
@Service
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"searchHot")
public class SearchHotService extends BaseService<SearchHot> {

    @Autowired
    private SearchHotMapper searchHotMapper;

    /**
     * 更新或者新增搜索关键词
     * @param type 类型:(1:表情;2:用户;3:素材)
     * @param keyword 关键词
     * @return SearchHot
     */
    public SearchHot updateByKeyword(Integer type,String keyword) {
        SearchHot record = selectByKeyword(keyword);
        // 判断关键词是否存在
        if (record != null) {
            // 关键词搜索次数+1
            record.setSearchNum(record.getSearchNum()+1);
        } else {
            // 新增搜索关键词
            record = new SearchHot();
            record.setText(keyword);
            record.setSearchNum(1);
        }
        record.setType(type);
        return super.update(record);
    }


    /**
     * 根据关键词获取对象
     * @param keyword 关键词
     * @return
     */
    public SearchHot selectByKeyword(String keyword) {
        String id = searchHotMapper.selectByKeyword(keyword);
        if (StringUtils.isNotBlank(id)) {
            return queryById(id);
        }
        return null;
    }
}