package com.bq.shuo.support;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.util.InstanceUtil;
import com.bq.shuo.model.User;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UserHelper
 *
 * @author Harvey.Wei
 * @date 2016/10/15 0015
 */
public final class UserHelper {
    private UserHelper() {}

    public static List<String> findAtUser(String str) {
        List<String> arrayList = InstanceUtil.newArrayList();
        if (StringUtils.isNotBlank(str)) {
            String regex = "@(.*?)((?=@)|\\s|$)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);//匹配类
            while (matcher.find()) {
                String val = matcher.group(1).trim();
                if (StringUtils.isNotBlank(val)) {
                    arrayList.add(val);
                }
            }
        }
        return arrayList;
    }

    public static Map<String,Object> formatResultPage(Page<User> pageInfo) {
        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        resultMap.put("total",pageInfo.getTotal());
        resultMap.put("size",pageInfo.getSize());
        resultMap.put("pages",pageInfo.getPages());
        resultMap.put("current",pageInfo.getCurrent());
        resultMap.put("records",formatResultList(pageInfo.getRecords()));
        return  resultMap;
    }

    public static List<Map<String,Object>> formatResultList(List records) {
        List<Map<String,Object>> resultList = InstanceUtil.newArrayList();
        for (Object obj: records) {
            User user = (User) obj;
            resultList.add(formatResultMap(user));
        }
        return resultList;
    }

    public static List<Map<String,Object>> formatFollowResultList(List records) {
        List<Map<String,Object>> resultList = InstanceUtil.newArrayList();
        for (Object obj: records) {
            User user = (User) obj;
            resultList.add(formatFollowResultMap(user));
        }
        return resultList;
    }


    public static Map<String,Object> formatResultMap(User user) {
        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        if (user != null) {
            resultMap.put("uid",user.getId());
            resultMap.put("name",user.getName());
            resultMap.put("avatar",user.getAvatar());
            resultMap.put("sex",user.getSex());
            resultMap.put("type",user.getUserType());
            resultMap.put("summary",user.getSummary());
            resultMap.put("address",user.getAddress());
            resultMap.put("birthday",user.getBirthday());
            resultMap.put("worksNum",user.getWorksNum());
            resultMap.put("worksLikeNum",user.getWorksLikeNum());
            resultMap.put("myLikeWorksNum",user.getMyLikeWorksNum());
            resultMap.put("forwardNum",user.getForwardNum());
            resultMap.put("followNum",user.getFollowNum());
            resultMap.put("fansNum",user.getFansNum());
            resultMap.put("lastDynamicTime", user.getLastDynamicTime() != null ? user.getLastDynamicTime().getTime() : user.getLastDynamicTime());
            resultMap.put("createTime",user.getCreateTime().getTime());
            resultMap.put("isMessage", user.isMessage());
            resultMap.put("isFollow",user.isFollow());
            resultMap.put("isPhone",StringUtils.isNotBlank(user.getPhone()));
        }
        return resultMap;
    }

    public static Map<String,Object> formatBriefResultMap(User record) {
        if (record != null) {
            Map<String, Object> resultMap = InstanceUtil.newHashMap();
            resultMap.put("uid", record.getId());
            resultMap.put("name", record.getName());
            resultMap.put("avatar", record.getAvatar());
            resultMap.put("sex", record.getSex());
            resultMap.put("type", record.getUserType());
            resultMap.put("summary", record.getSummary());
            resultMap.put("isFollow", record.isFollow());
            return resultMap;
        }
        return InstanceUtil.newHashMap();
    }

    public static Map<String,Object> formatFollowResultMap(User user) {
        Map<String,Object> resultMap = formatResultMap(user);
        resultMap.put("isFollow",user.isFollow());
        return resultMap;
    }

    public static Map<String, Object> formatLoginResultMap(String token, String currUser, String nickname,String avatar) {
        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        resultMap.put("token",token);
        resultMap.put("userId",currUser);
        resultMap.put("nickname", nickname);
        resultMap.put("avatar", avatar);
        return resultMap;
    }

    public static Map<String, Object> formatListResultMap(User user) {
        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        resultMap.put("uid",user.getId());
        resultMap.put("name",user.getName());
        resultMap.put("avatar",user.getAvatar());
        resultMap.put("type",user.getUserType());
        resultMap.put("lastDynamicTime",user.getLastDynamicTime() != null ? user.getLastDynamicTime().getTime() : user.getLastDynamicTime());
        return resultMap;
    }
}
