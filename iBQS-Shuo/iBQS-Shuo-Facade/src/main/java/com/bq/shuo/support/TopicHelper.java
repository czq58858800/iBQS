package com.bq.shuo.support;

import com.bq.core.util.InstanceUtil;
import com.bq.shuo.model.Topics;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TopicHelper
 *
 * @author Harvey.Wei
 * @date 2016/10/21 0021
 */
public final class TopicHelper {
    private TopicHelper() {}

    public static List<String> findTopic(String str) {
        List<String> topic = InstanceUtil.newArrayList();
        if (StringUtils.isNotBlank(str)) {
            String regex = "#(.*?)#";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);//匹配类
            while (matcher.find()) {
                topic.add(matcher.group(1));
            }
        }
        return topic;
    }

    public static List<Map<String,Object>> formatResultList(List<Topics> list) {
        List<Map<String, Object>> resultList = InstanceUtil.newArrayList();
        for (Topics record : list) {
            resultList.add(formatResultMap(record));
        }
        return resultList;
    }

    public static Map<String,Object> formatResultMap(Topics record) {
        if (record != null) {
            Map<String,Object> resultMap = InstanceUtil.newHashMap();
            if (record.getId() != null) {
                resultMap.put("uid",record.getId());
                resultMap.put("summary",record.getSummary());
                resultMap.put("name",record.getName());
            }
            resultMap.put("cover",record.getCover());
            resultMap.put("createTime",record.getCreateTime().getTime());
            resultMap.put("banner",record.getBanner());
            resultMap.put("audit",record.getAudit());
            resultMap.put("status",record.getOwnerStatus());
            resultMap.put("viewNum",record.getViewNum());
            if (record.getOwner() != null) {
                Map<String,Object> userMap = InstanceUtil.newHashMap();
                userMap.put("uid",record.getOwner().getId());
                userMap.put("name",record.getOwner().getName());
                userMap.put("avatar",record.getOwner().getAvatar());
                userMap.put("type",record.getOwner().getUserType());
                userMap.put("isFollow",record.getOwner().isFollow());
                userMap.put("sex",record.getOwner().getSex());
                resultMap.put("user",userMap);
            }
            Map<String,Object> coverExifMap = InstanceUtil.newHashMap();
            coverExifMap.put("width",record.getCoverWidth());
            coverExifMap.put("type",record.getCoverType());
            coverExifMap.put("height",record.getCoverHeight());
            resultMap.put("coverExif",coverExifMap);
            return resultMap;
        }
        return null;
    }

    public static List<Map<String,Object>> formatListMap(List<Topics> list) {
        List<Map<String,Object>> resultList = InstanceUtil.newArrayList();
        for (Topics record:list) {
            resultList.add(formatTopicMap(record));
        }
        return resultList;
    }

    public static Map<String,Object> formatTopicMap(Topics record) {
        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        resultMap.put("uid",record.getId());
        resultMap.put("name",record.getName());
        resultMap.put("summary",record.getSummary());
        resultMap.put("banner",record.getBanner());
        resultMap.put("cover",record.getCover());
        Map<String,Object> coverExifMap = InstanceUtil.newHashMap();
        coverExifMap.put("width",record.getCoverWidth());
        coverExifMap.put("type",record.getCoverType());
        coverExifMap.put("height",record.getCoverHeight());
        resultMap.put("coverExif",coverExifMap);
        return resultMap;
    }
}
