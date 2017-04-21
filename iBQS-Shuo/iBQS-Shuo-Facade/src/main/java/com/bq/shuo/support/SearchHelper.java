package com.bq.shuo.support;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.util.InstanceUtil;
import com.bq.shuo.model.SearchHot;

import java.util.List;
import java.util.Map;

/**
 * SearchHelper
 *
 * @author Harvey.Wei
 * @date 2016/11/2 0002
 */
public final class SearchHelper {
    private SearchHelper(){}

    public static Map<String,Object> formatResultPage(Page<SearchHot> pageInfo) {
        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        resultMap.put("total",pageInfo.getTotal());
        resultMap.put("size",pageInfo.getSize());
        resultMap.put("pages",pageInfo.getPages());
        resultMap.put("current",pageInfo.getCurrent());
        resultMap.put("records",pageInfo.getRecords());
        return  resultMap;
    }

    public static Map<String,Object> formatSearchHotResultPage(Page<SearchHot> pageInfo) {
        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        resultMap.put("total",pageInfo.getTotal());
        resultMap.put("size",pageInfo.getSize());
        resultMap.put("pages",pageInfo.getPages());
        resultMap.put("current",pageInfo.getCurrent());
        resultMap.put("records",formatSearchHotResultList(pageInfo.getRecords()));
        return  resultMap;
    }

    public static List<Map<String,Object>> formatSearchHotResultList(List records) {
        List<Map<String,Object>> resultList = InstanceUtil.newArrayList();
        for (Object obj: records) {
            SearchHot record = (SearchHot) obj;
            resultList.add(formatSearchHotResultMap(record));
        }
        return resultList;
    }

    public static Map<String,Object> formatSearchHotResultMap(SearchHot record) {
        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        resultMap.put("uid",record.getId());
        resultMap.put("searchNum",record.getSearchNum());
        resultMap.put("keyword",record.getText());
        resultMap.put("createTime",record.getCreateTime().getTime());
        return resultMap;
    }


}
