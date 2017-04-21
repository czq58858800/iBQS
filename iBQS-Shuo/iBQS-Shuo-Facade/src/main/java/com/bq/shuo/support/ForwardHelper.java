package com.bq.shuo.support;

import com.bq.core.util.InstanceUtil;
import com.bq.shuo.model.Forward;
import com.bq.shuo.model.User;

import java.util.List;
import java.util.Map;

/**
 * ForwardHelper
 *
 * @author Harvey.Wei
 * @date 2016/11/24 0024
 */
public final class ForwardHelper {
    public static List<Map<String,Object>> formatResultList(List records) {
        List<Map<String,Object>> resultList = InstanceUtil.newArrayList();
        for (Object obj: records) {
            Forward record = (Forward) obj;
            resultList.add(formatResultMap(record));
        }
        return resultList;
    }

    public static Map<String,Object> formatResultMap(Forward record) {
        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        resultMap.put("uid", record.getId());
        resultMap.put("dynType",2);
        resultMap.put("content",record.getContent());
        resultMap.put("publishTime",record.getCreateTime().getTime());
        if (record.getUser() != null) {
            Map<String,Object> userMap = InstanceUtil.newHashMap();
            User user = record.getUser();
            userMap.put("uid",user.getId());
            userMap.put("name",user.getName());
            userMap.put("avatar",user.getAvatar());
            userMap.put("sex",user.getSex());
            userMap.put("summary",user.getSummary());
            userMap.put("type",user.getUserType());
            userMap.put("lastDynamicTime",user.getLastDynamicTime() != null ? user.getLastDynamicTime().getTime() : user.getLastDynamicTime());
            resultMap.put("user", userMap);
        }
        resultMap.put("forward",SubjectHelper.formatResultMap(record.getSubject()));
        return resultMap;
    }
}
