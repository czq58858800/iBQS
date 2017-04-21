package com.bq.shuo.support;


import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.util.InstanceUtil;
import com.bq.shuo.model.Category;
import com.bq.shuo.model.CategoryCollection;
import com.bq.shuo.model.User;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/17.
 */
public final class CategoryHelper {

    public static List<Map<String,Object>> formatCategoryResultList(List records) {
        List<Map<String,Object>> resultList = InstanceUtil.newArrayList();
        for (Object obj: records) {
            if (obj instanceof CategoryCollection) {
                CategoryCollection record = (CategoryCollection) obj;
                resultList.add(formatCategoryCollResultMap(record));
            }

            if (obj instanceof Category) {
                Category record = (Category) obj;
                resultList.add(formatCategoryResultMap(record));
            }

        }
        return resultList;
    }

    public static Map<String, Object> formatCategoryCollResultMap(CategoryCollection record) {
        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        resultMap.put("uid",record.getId());
        resultMap.put("createTime",record.getCreateTime().getTime());
        if (record.getCategory() != null) {
            Category category = record.getCategory();
            Map<String,Object> categoryMap = formatCategoryResultMap(category);
            resultMap.put("category",categoryMap);
        }
        return resultMap;
    }

    public static Map<String, Object> formatCategoryResultMap(Category record) {
        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        resultMap.put("uid",record.getId());
        resultMap.put("cover",record.getCover());
        resultMap.put("coverType",record.getCoverType());
        resultMap.put("name",record.getName());
        resultMap.put("summary",record.getSummary());
        resultMap.put("tags",record.getTags());
        resultMap.put("isColl",record.isColl());
        resultMap.put("audit",record.getAudit());

        resultMap.put("tags",record.getTags());
        if (record.getAuthor() != null) {
            User author = record.getAuthor();
            Map<String,Object> authorMap = InstanceUtil.newHashMap();
            authorMap.put("uid",author.getId());
            authorMap.put("name",author.getName());
            authorMap.put("avatar",author.getAvatar());
            authorMap.put("type",author.getUserType());
            authorMap.put("isFollow",author.isFollow());
            resultMap.put("user",authorMap);
        }
        return resultMap;

    }

    public static Map formatCategoryResultPage(Page page) {
        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        resultMap.put("total",page.getTotal());
        resultMap.put("pages",page.getPages());
        resultMap.put("current",page.getCurrent());
        resultMap.put("size",page.getSize());
        resultMap.put("records",formatCategoryResultList(page.getRecords()));
        return resultMap;
    }
}
