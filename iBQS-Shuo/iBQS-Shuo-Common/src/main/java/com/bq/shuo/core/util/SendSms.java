package com.bq.shuo.core.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsRequest;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsResponse;
import com.bq.core.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * SendSms
 *
 * @author Harvey.Wei
 * @date 2016/10/15 0015
 */
public class SendSms {

    private static final String REGION_ID = PropertiesUtil.getString("aliyun.sms.region.id");
    private static final String ACCESS_KEY = PropertiesUtil.getString("aliyun.access.key");
    private static final String ACCESS_SECRET = PropertiesUtil.getString("aliyun.access.secret");
    private static final String ENDPOINT_NAME = PropertiesUtil.getString("aliyun.sms.endpoint.name");
    private static final String PRODUCT = PropertiesUtil.getString("aliyun.sms.product");
    private static final String DOMAIN = PropertiesUtil.getString("aliyun.sms.domain");
    private final static String SIGN_NAME = PropertiesUtil.getString("aliyun.sms.sign.name"); // 签名名称从控制台获取，必须是审核通过的
    private final static String VERIFY_TPL_CODE = PropertiesUtil.getString("aliyun.sms.verify.tpl.code"); //模板CODE从控制台获取，必须是审核通过的

    public static boolean sendVerifyCode(String phone,int code) {
        IClientProfile profile = DefaultProfile.getProfile(REGION_ID, ACCESS_KEY, ACCESS_SECRET);
        try {
            DefaultProfile.addEndpoint(ENDPOINT_NAME, REGION_ID, PRODUCT,  DOMAIN);
            IAcsClient client = new DefaultAcsClient(profile);
            SingleSendSmsRequest request = new SingleSendSmsRequest();
            request.setSignName(SIGN_NAME);
            request.setTemplateCode(VERIFY_TPL_CODE);
            StringBuffer paramStr = new StringBuffer();
            paramStr.append("{'code':'").append(code).append("'}");
            request.setParamString(paramStr.toString());
            request.setRecNum(phone);
            SingleSendSmsResponse httpResponse = client.getAcsResponse(request);
            if (StringUtils.isNotBlank(httpResponse.getModel())) {
                return true;
            }
        } catch (ServerException e) {
            e.printStackTrace();
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        SendSms.sendVerifyCode("18950037411",1111);
    }
}
