package com.bq.shuo.core.util;

import com.bq.core.util.PropertiesUtil;
import com.qcloud.sms.SmsSingleSender;
import com.qcloud.sms.SmsSingleSenderResult;

import java.util.ArrayList;

/**
 * SendSms
 *
 * @author Harvey.Wei
 * @date 2016/10/15 0015
 */
public class SendSms {

    private static final int APP_ID = PropertiesUtil.getInt("qcloud.sms.appid");
    private static final String APP_KEY = PropertiesUtil.getString("qcloud.sms.appkey");

    public static SmsSingleSenderResult sendVerifyCode(String phone,int code) {
        try {
            int tmplId = 26193;

            //初始化单发
            SmsSingleSender singleSender = new SmsSingleSender(APP_ID, APP_KEY);
            SmsSingleSenderResult singleSenderResult;

            //指定模板单发
            //假设短信模板 id 为 1，模板内容为：测试短信，{1}，{2}，{3}，上学。
            ArrayList<String> params = new ArrayList<>();
            params.add(String.valueOf(code));
            singleSenderResult = singleSender.sendWithParam("86", phone, tmplId, params, "表情说说", "", "");
            return singleSenderResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        SendSms.sendVerifyCode("18950037411",1111);
    }
}
