package com.bq.shuo.support;

import com.bq.core.util.InstanceUtil;
import com.bq.shuo.model.Notify;
import com.bq.shuo.model.User;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/24.
 */
public final class NotifyHelper {

    public static List<Map<String,Object>> formatResultList(List records) {
        List<Map<String,Object>> resultList = InstanceUtil.newArrayList();
        for (Object obj: records) {
            Notify record = (Notify) obj;
            resultList.add(formatResultMap(record));
        }
        return resultList;
    }

    public static Map<String,Object> formatResultMap(Notify record) {
        Map<String, Object> resultMap = InstanceUtil.newHashMap();
        if (record != null && StringUtils.isNotBlank(record.getId())) {
            resultMap.put("uid", record.getId());
            resultMap.put("cover",record.getSubject().getCover());
            resultMap.put("coverType",record.getSubject().getCoverType());
            resultMap.put("subjectId", record.getSubjectId());
            resultMap.put("content", record.getContent());
            resultMap.put("msgType", record.getMsgType());
            resultMap.put("receiveUserId", record.getReceiveUserId());
            resultMap.put("createTime",record.getCreateTime().getTime());
            resultMap.put("isRead",record.getIsRead());
            if (record.getSendUser() != null) {
                Map<String,Object> userMap = InstanceUtil.newHashMap();
                User sendUser = record.getSendUser();
                userMap.put("uid",sendUser.getId());
                userMap.put("name",sendUser.getName());
                userMap.put("avatar",sendUser.getAvatar());
                userMap.put("sex",sendUser.getSex());
                userMap.put("type",sendUser.getUserType());
                resultMap.put("sendUser",userMap);
            }
        }
        return resultMap;
    }
}
