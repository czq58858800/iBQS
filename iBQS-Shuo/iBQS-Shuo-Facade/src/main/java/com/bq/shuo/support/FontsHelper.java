package com.bq.shuo.support;

import com.bq.core.util.InstanceUtil;
import com.bq.shuo.model.Fonts;

import java.util.List;
import java.util.Map;

/**
 * CommentsHelper
 *
 * @author chern.zq
 * @date 2016/11/14 0014
 */
public final class FontsHelper {

    public static List<Map<String,Object>> formatResultList(List records) {
        List<Map<String,Object>> resultList = InstanceUtil.newArrayList();
        for (Object obj: records) {
            Fonts record = (Fonts) obj;
            resultList.add(formatResultMap(record));
        }
        return resultList;
    }

    public static Map<String,Object> formatResultMap(Fonts record) {
        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        resultMap.put("uid",record.getId());
        resultMap.put("name",record.getName());
        resultMap.put("cover",record.getCover());
        resultMap.put("font",record.getFont());
        resultMap.put("lang",record.getLang());
        return resultMap;
    }
}
