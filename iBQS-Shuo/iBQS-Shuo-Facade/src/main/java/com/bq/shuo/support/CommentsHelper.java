package com.bq.shuo.support;


import com.bq.core.util.InstanceUtil;
import com.bq.shuo.model.Comments;
import com.bq.shuo.model.User;

import java.util.List;
import java.util.Map;

/**
 * CommentsHelper
 *
 * @author chern.zq
 * @date 2016/11/14 0014
 */
public final class CommentsHelper {

    public static List<Map<String,Object>> formatResultList(List records) {
        List<Map<String,Object>> resultList = InstanceUtil.newArrayList();
        for (Object obj: records) {
            Comments record = (Comments) obj;
            resultList.add(formatResultMap(record));
        }
        return resultList;
    }

    public static Map<String,Object> formatResultMap(Comments record) {
        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        //        JSONObject exif = cover.contains("?") ? JSONObject.fromObject(cover.substring(cover.indexOf("?")+6)) : null;
        resultMap.put("uid",record.getId());
        resultMap.put("content",record.getContent());
        resultMap.put("isPraise",record.isPraise());
        resultMap.put("praiseNum",record.getPraiseNum());
        resultMap.put("commentTime",record.getCreateTime().getTime());
        if (record.getReplyUser() != null) {
            User replyUser = record.getReplyUser();
            Map<String,Object> replyUserMap = InstanceUtil.newHashMap();

            replyUserMap.put("uid",replyUser.getId());
            replyUserMap.put("name",replyUser.getName());
            replyUserMap.put("avatar",replyUser.getAvatar());
            replyUserMap.put("type",replyUser.getUserType());
            resultMap.put("replyUser",replyUserMap);
        }

        if (record.getBeReplyUser() != null) {
            User beReplyUser = record.getBeReplyUser();
            Map<String,Object> beReplyUserMap = InstanceUtil.newHashMap();

            beReplyUserMap.put("uid",beReplyUser.getId());
            beReplyUserMap.put("name",beReplyUser.getName());
            beReplyUserMap.put("avatar",beReplyUser.getAvatar());
            beReplyUserMap.put("type",beReplyUser.getUserType());
            resultMap.put("beReplyUser",beReplyUserMap);
        }
        return resultMap;
    }
}
