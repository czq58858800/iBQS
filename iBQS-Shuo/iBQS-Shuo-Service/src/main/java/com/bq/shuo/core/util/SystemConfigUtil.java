package com.bq.shuo.core.util;

import com.bq.core.Constants;
import com.bq.core.util.CacheUtil;
import com.bq.core.util.InstanceUtil;
import com.bq.shuo.service.SystemConfigService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

/**
 * Created by Administrator on 2017/4/20 0020.
 */
public class SystemConfigUtil {

    public static final String SUBJECT_HOT_SORT_NUM = "SUBJECT_HOT_SORT_NUM";
    public static final String INDEX_SUBJECT_HOT_SORT_NUM = "INDEX_SUBJECT_HOT_SORT_NUM";
    public static final String SUBJECT_LIKED_HOT_NUM = "SUBJECT_LIKED_HOT_NUM";
    public static final String ALBUM_LIKED_HOT_NUM = "ALBUM_LIKED_HOT_NUM";
    public static final String CATEGORY_COLLECTION_HOT_NUM = "CATEGORY_COLLECTION_HOT_NUM";
    public static final String STICKER_CITES_NUM = "STICKER_CITES_NUM";

    public static Integer getIntValue(String field) {

        if (!CacheUtil.getCache().exists(Constants.SYSTEM_CONFIG_CACHA)){
            ContextLoader.getCurrentWebApplicationContext().getBean(SystemConfigService.class).init();
        }
        String value = (String) CacheUtil.getCache().hget(Constants.SYSTEM_CONFIG_CACHA,field);
        return Integer.parseInt(value);
    }
}
