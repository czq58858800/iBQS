package com.bq.shuo.core.util;

import com.alibaba.fastjson.JSONObject;
import com.bq.core.config.Resources;
import com.bq.core.util.HttpUtil;
import com.bq.core.util.InstanceUtil;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/16.
 */
public class WeiboHelper {

    private static final Logger logger = LogManager.getLogger();

    /**
     * 发布一条新微博
     *
     * @param token
     * @param status
     */
    public static final void sendWeibo(String token, String status) throws Exception {
        String url = Resources.THIRDPARTY.getString("setStatusesUploadURL_sina");
        ArrayList<NameValuePair> params = InstanceUtil.newArrayList();
        params.add(new NameValuePair("access_token",token));
        params.add(new NameValuePair("status",status));
        String res = HttpUtil.httpClientPost(url,params);
        JSONObject json = JSONObject.parseObject(res);
        logger.debug("发布一条新微博："+res);
    }
}
