package com.bq.shuo.web;

import com.bq.core.support.HttpCode;
import com.bq.core.util.InstanceUtil;
import com.bq.core.util.PropertiesUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.provider.IShuoProvider;
import com.qiniu.util.Auth;
import io.rong.RongCloud;
import io.rong.models.TokenReslut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * ThirdPartyTokenController
 *
 * @author Harvey.Wei
 * @date 2016/12/20 0020
 */
@Controller
@Api(value = "第三方Token获取接口", description = "第三方Token获取接口")
public class ThirdPartyTokenController extends AbstractController<IShuoProvider> {

    @Override
    public String getService() {
        return "userService";
    }

    @ApiOperation(value = "获取七牛上传Token")
    @GetMapping("/qiniuUpToken")
    public Object qiniuUpToken(ModelMap modelMap) {
        Auth auth = Auth.create(PropertiesUtil.getString("qiniu.access_key"), PropertiesUtil.getString("qiniu.secret_key"));
        Map<String,Object> dataMap = InstanceUtil.newHashMap();
        dataMap.put("uptoken",auth.uploadToken(PropertiesUtil.getString("qiniu.bucket")));
        dataMap.put("domain",PropertiesUtil.getString("qiniu.domain"));
        return setSuccessModelMap(modelMap,dataMap);
    }

    @ApiOperation(value = "获取融云用户Token")
    @GetMapping("/rongUserToken")
    public Object rongUserToken(ModelMap modelMap,
            @ApiParam(required = true, value = "用户ID") @RequestParam(value = "userId") String userId,
            @ApiParam(required = true, value = "用户名称") @RequestParam(value = "name") String name,
            @ApiParam(required = true, value = "用户头像") @RequestParam(value = "avatar") String avatar) {
        RongCloud rongCloud = RongCloud.getInstance(PropertiesUtil.getString("rong.access_key"), PropertiesUtil.getString("rong.secret_key"));
        try {
            TokenReslut userGetTokenResult = rongCloud.user.getToken(userId, name, avatar);
            Map<String,Object> dataMap = InstanceUtil.newHashMap();
            modelMap.put("code",userGetTokenResult.getCode());
            dataMap.put("token",userGetTokenResult.getToken());
            modelMap.put("msg",userGetTokenResult.getErrorMessage());
            return setSuccessModelMap(modelMap,dataMap);
        } catch (Exception e) {
            e.printStackTrace();
            return setModelMap(modelMap, HttpCode.INTERNAL_SERVER_ERROR);
        }
    }
}
