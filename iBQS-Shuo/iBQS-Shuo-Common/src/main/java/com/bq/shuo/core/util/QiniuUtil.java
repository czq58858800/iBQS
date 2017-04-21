package com.bq.shuo.core.util;

import com.alibaba.fastjson.JSONObject;
import com.bq.core.util.HttpUtil;

/**
 * Created by Administrator on 2017/1/21.
 */
public final class QiniuUtil {

    public static JSONObject getImageInfo(String image) {
        image = image + "?imageInfo";
        String res = HttpUtil.httpClientPost(image);
        return JSONObject.parseObject(res);
    }
}
