package com.bq.shuo.support;

import com.bq.core.util.InstanceUtil;
import com.bq.shuo.model.SubjectLiked;
import com.bq.shuo.model.User;

import java.util.List;
import java.util.Map;

/**
 * SubjectLikedHelper
 *
 * @author chern.zq
 * @date 2016/11/24 0024
 */
public final class SubjectLikedHelper {
    private SubjectLikedHelper(){}

    public static List<Map<String,Object>> formatResultList(List records) {
        List<Map<String,Object>> resultList = InstanceUtil.newArrayList();
        for (Object obj: records) {
            SubjectLiked record = (SubjectLiked) obj;
            resultList.add(formatResultMap(record));
        }
        return resultList;
    }

    public static Map<String,Object> formatResultMap(SubjectLiked record) {
        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        if (record==null) return null;
        resultMap.put("uid",record.getId());
        resultMap.put("subjectId",record.getSubjectId());
        resultMap.put("likedTime",record.getCreateTime().getTime());
        resultMap.put("subject",SubjectHelper.formatResultMap(record.getSubject()));
        return resultMap;
    }
}
