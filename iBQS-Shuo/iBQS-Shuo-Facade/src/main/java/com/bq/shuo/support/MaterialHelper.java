package com.bq.shuo.support;


import com.bq.core.util.InstanceUtil;
import com.bq.shuo.model.Category;
import com.bq.shuo.model.Material;
import com.bq.shuo.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * MaterialHelper
 *
 * @author chern.zq
 * @date 2016/11/29 0029
 */
public final class MaterialHelper {

    public static List<Map<String,Object>> formatCategoryResultList(List records) {
        List<Map<String,Object>> resultList = InstanceUtil.newArrayList();
        for (Object obj: records) {
            Category record = (Category) obj;
            resultList.add(formatCategoryResultMap(record));
        }
        return resultList;
    }

    public static Map<String,Object> formatCategoryResultMap(Category record) {
        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        resultMap.put("uid",record.getId());
        resultMap.put("cover",record.getCover());
        resultMap.put("coverType",record.getCoverType());
        resultMap.put("name",record.getName());
        resultMap.put("summary",record.getSummary());
        resultMap.put("tags",record.getTags());
        resultMap.put("stuffNum",record.getStuffNum());
        resultMap.put("viewNum",record.getViewNum());
        resultMap.put("createTime",record.getCreateTime().getTime());
        resultMap.put("isColl",record.isColl());
        List<Material> stuffs = record.getStuffs();
        if (stuffs != null && stuffs.size() != 0) {

            List<Map<String,Object>> resultStuffs = InstanceUtil.newArrayList();
            for (Material stuff : stuffs) {
                Map<String,Object> stuffMap = InstanceUtil.newHashMap();
                stuffMap.put("uid",stuff.getId());
                stuffMap.put("image",stuff.getImage());
                stuffMap.put("citations",stuff.getCitations());
                stuffMap.put("type",stuff.getImageType());
                stuffMap.put("width",stuff.getImageWidth());
                stuffMap.put("height",stuff.getImageHeight());
                resultStuffs.add(stuffMap);
            }
//            Collections.shuffle(resultStuffs);
            resultMap.put("stuffs",resultStuffs);
        }

        if (record.getAuthor() != null) {
            Map<String,Object> userMap = InstanceUtil.newHashMap();
            User author = record.getAuthor();
            userMap.put("uid",author.getId());
            userMap.put("name",author.getName());
            userMap.put("avatar",author.getAvatar());
            userMap.put("type",author.getUserType());
            userMap.put("isFollow",author.isFollow());
            resultMap.put("user",userMap);
        }
        return resultMap;
    }
}
