package com.bq.shuo.web;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.Constants;
import com.bq.core.config.Resources;
import com.bq.core.support.Assert;
import com.bq.core.support.HttpCode;
import com.bq.core.util.CacheUtil;
import com.bq.core.util.InstanceUtil;
import com.bq.core.util.SerializeUtil;
import com.bq.core.util.WebUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.core.helper.PushType;
import com.bq.shuo.core.support.login.ThirdPartyLoginHelper;
import com.bq.shuo.core.support.login.ThirdPartyUser;
import com.bq.shuo.model.*;
import com.bq.shuo.provider.IShuoProvider;
import com.bq.shuo.support.UserHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * UserController
 *
 * @author Harvey.Wei
 * @date 2016/10/15 0015
 */
@RestController
@Api(value = "用户接口", description = "用户接口")
@RequestMapping(value = "user")
public class UserController extends AbstractController<IShuoProvider> {
    @Override
    public String getService() {
        return "userService";
    }

    // 推荐用户
    @ApiOperation(value = "推荐用户")
    @GetMapping(value = "rcmd/list")
    public Object searchUser(HttpServletRequest request, ModelMap modelMap,
             @ApiParam(required = true, value = "分页") @RequestParam(value = "pageNum") int pageNum) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        params.put("notNullLastDynamicTime",true);
        params.put("notInBeFollowUser",getCurrUser());
        params.put("currUserId",getCurrUser());
        Parameter parameter = new Parameter(getService(),"queryBeans").setMap(params);
        Page page = provider.execute(parameter).getPage();
        List<Map<String,Object>> resultList = InstanceUtil.newArrayList();
        for (Object obj:page.getRecords()) {
            User record = (User) obj;
            Map<String,Object> resultMap = InstanceUtil.newHashMap();
            resultMap.put("uid",record.getId());
            resultMap.put("name",record.getName());
            resultMap.put("avatar",record.getAvatar());
            resultMap.put("type",record.getUserType());
            resultMap.put("summary",record.getSummary());
            resultMap.put("fansNum",record.getFansNum());
            resultMap.put("isFollow",record.isFollow());
            resultList.add(resultMap);
        }
        page.setRecords(resultList);
        return setSuccessModelMap(modelMap,page);
    }


    // 用户个人信息
    @ApiOperation(value = "用户个人信息")
    @GetMapping(value = "detail")
    public Object detail(HttpServletRequest request, ModelMap modelMap,
             @ApiParam(required = false, value = "用户ID") @RequestParam(value = "userId",required = false) String userId) {

        if (StringUtils.isBlank(userId) ) {
            if (StringUtils.isBlank(getCurrUser())) {
                return setModelMap(modelMap, HttpCode.UNAUTHORIZED);
            }
            userId = getCurrUser();
        }

        User user = (User) provider.execute(new Parameter("userService","queryBeanById").setObjects(new Object[]{userId,getCurrUser()})).getModel();
        if (user == null) {
            return setModelMap(modelMap, HttpCode.USER_NON_EXISTENT);
        }
        Map<String,Object> resultMap = UserHelper.formatResultMap(user);
        resultMap.put("isMessage",user.isMessage());
        if (StringUtils.isNotBlank(userId)) {

            List<UserThirdparty> thirdpartyList = (List<UserThirdparty>) provider.execute(new Parameter("userThirdpartyService","queryThirdPartByList").setId(userId)).getList();
            List<Map<String,Object>> thirdpartyMapList = InstanceUtil.newArrayList();
            if (thirdpartyList != null && thirdpartyList.size() > 0) {
                for (UserThirdparty thirdparty:thirdpartyList) {
                    Map<String,Object> thirdpartyMap = InstanceUtil.newHashMap();
                    thirdpartyMap.put("avatar",thirdparty.getAvatar());
                    thirdpartyMap.put("name",thirdparty.getName());
                    thirdpartyMap.put("provider",thirdparty.getProvider());
                    thirdpartyMap.put("verified", thirdparty.getVerified());
                    thirdpartyMap.put("verifiedReason", thirdparty.getVerifiedReason());
                    thirdpartyMapList.add(thirdpartyMap);
                }
                resultMap.put("thirdParty",thirdpartyMapList);
            }
        }

        return setSuccessModelMap(modelMap, resultMap);
    }

    /**
     * 修改用户信息
     * @param name 昵称
     * @param oldPassword 旧密码
     * @param password 密码
     * @param sex 性别
     * @param summary 简介
     * @return
     */
    @ApiOperation(value = "修改用户信息")
    @PostMapping(value = "modify")
    public Object update(HttpServletRequest request, ModelMap modelMap,
             @ApiParam(required = false, value = "昵称") @RequestParam(value = "name",required = false) String name,
             @ApiParam(required = false, value = "头像") @RequestParam(value = "avatar",required = false) String avatar,
             @ApiParam(required = false, value = "旧密码") @RequestParam(value = "oldPassword",required = false) String oldPassword,
             @ApiParam(required = false, value = "密码") @RequestParam(value = "password",required = false) String password,
             @ApiParam(required = false, value = "性别") @RequestParam(value = "sex",required = false) Integer sex,
             @ApiParam(required = false, value = "生日") @RequestParam(value = "birthday",required = false) String birthday,
             @ApiParam(required = false, value = "地址") @RequestParam(value = "address",required = false) String address,
             @ApiParam(required = false, value = "简介") @RequestParam(value = "summary",required = false) String summary) {

        User user = new User(getCurrUser(),name,avatar,password,sex,birthday,address,summary);

        if (StringUtils.isNotBlank(name)) {
            Map<String, Object> params = InstanceUtil.newHashMap();
            params.put("name", name);
            Parameter parameter = new Parameter("userService","selectCheck").setMap(params);
            boolean nicknameIsExist = (boolean) provider.execute(parameter).getObject();

            if(nicknameIsExist) {
                return setModelMap(modelMap,HttpCode.REG_HAS_NAME);
            }
        }

        if (StringUtils.isNotBlank(password)) {
            User u = (User) provider.execute(new Parameter("userService","queryById").setId(getCurrUser())).getModel();
            if (StringUtils.isBlank(u.getPassword())) {
                user.setPassword(password);
            } else if (StringUtils.equals(oldPassword,u.getPassword())) {
                user.setPassword(password);
            } else {
                return setModelMap(modelMap, HttpCode.PASSWORD_NOT_CONSISTENT);
            }
        }
        provider.execute(new Parameter("userService","update").setModel(user));
        return setSuccessModelMap(modelMap);
    }

    // 关注用户
    @ApiOperation(value = "关注用户")
    @PostMapping(value = "following")
    public Object following(HttpServletRequest request, ModelMap modelMap,
                         @ApiParam(required = true, value = "被关注用户ID") @RequestParam(value = "beUserId") String beUserId,
                        @ApiParam(required = true, value = "关注:Y;取消:C") @RequestParam(value = "follow") String follow) {
        String userId = getCurrUser();

        follow = follow.trim().toUpperCase();
        if (StringUtils.equals(follow,"Y")) {
            // 不允许关注自己
            if (StringUtils.equals(userId,beUserId)) return setModelMap(modelMap,HttpCode.DONT_FOLLOW);

            User u = (User) provider.execute(new Parameter("userService","queryById").setId(getCurrUser())).getModel();

            if (u.getFollowNum() > 2000) {
                return setModelMap(modelMap,HttpCode.LIMIT_OF_ATTENTION);
            }

            Parameter parameter = new Parameter("userFollowingService","updateFollow").setObjects(new Object[] {userId,beUserId});
            boolean isFollow = (boolean) provider.execute(parameter).getObject();
            if (!isFollow){
                return setModelMap(modelMap,HttpCode.HAS_FOLLOW);
            }
        } else if (StringUtils.equals(follow,"C")) {
            Parameter parameter = new Parameter("userFollowingService","updateFollowCancel").setObjects(new Object[] {userId,beUserId});
            boolean isFollow = (boolean) provider.execute(parameter).getObject();
            if(!isFollow){
                return setModelMap(modelMap,HttpCode.NOT_FOLLOW);
            }
        } else {
            return setModelMap(modelMap,HttpCode.UNKNOWN_TYPE);
        }

        return setSuccessModelMap(modelMap);
    }

    // 绑定列表
    @ApiOperation(value = "绑定列表")
    @GetMapping(value = "bind/list")
    public Object bindWX(HttpServletRequest request, ModelMap modelMap) {

        List<UserThirdparty> thirdparties = (List<UserThirdparty>) provider.execute(new Parameter("userThirdpartyService","queryThirdPartByList").setId(getCurrUser())).getList();
        Map<String,Object> resultMap = InstanceUtil.newHashMap();

        if (thirdparties != null && thirdparties.size() > 0) {
            for (UserThirdparty record: thirdparties) {
                Map<String,Object> reMap = InstanceUtil.newHashMap();
                reMap.put("uid",record.getId());
                reMap.put("name",record.getName());
                reMap.put("isBind",true);
                resultMap.put(record.getProvider(),reMap);
            }
        }
        if (!resultMap.containsKey("WX")) {
            Map<String,Object> reMap = InstanceUtil.newHashMap();
            reMap.put("isBind",false);
            resultMap.put("WX",reMap);
        }
        if (!resultMap.containsKey("QQ")) {
            Map<String,Object> reMap = InstanceUtil.newHashMap();
            reMap.put("isBind",false);
            resultMap.put("QQ",reMap);
        }
        if (!resultMap.containsKey("SINA")) {
            Map<String,Object> reMap = InstanceUtil.newHashMap();
            reMap.put("isBind",false);
            resultMap.put("SINA",reMap);
        }

        User user = (User) provider.execute(new Parameter("userService","queryById").setId(getCurrUser())).getModel();
        if (user != null) {
            Map<String,Object> reMap = InstanceUtil.newHashMap();
            boolean flag = false;
            if (StringUtils.isNotBlank(user.getPhone())) {
                flag = true;
                reMap.put("uid",user.getId());
                reMap.put("name",user.getName());
                reMap.put("phone",user.getPhone());
            }
            reMap.put("isBind",flag);
            resultMap.put("PHONE",reMap);
        }


        return setSuccessModelMap(modelMap,resultMap);
    }

    // 绑定微信
    @ApiOperation(value = "绑定/解除微信")
    @PostMapping(value = "bind")
    public Object bind(HttpServletRequest request, ModelMap modelMap,
                 @ApiParam(required = false, value = "第三方ID") @RequestParam(value = "thirdId",required = false) String thirdId,
                 @ApiParam(required = true, value = "类型:{WX;QQ;SINA}") @RequestParam(value = "type",required = false) String type,
                 @ApiParam(required = true, value = "token") @RequestParam(value = "token",required = false) String token,
                 @ApiParam(required = true, value = "OpenId") @RequestParam(value = "openId",required = false) String openId) {
        UserThirdparty thirdpartyUser;

        // 解除绑定
        if (thirdId != null) {

            thirdpartyUser = (UserThirdparty) provider.execute(new Parameter("userThirdpartyService","queryById").setId(thirdId)).getModel();
            if (thirdpartyUser == null || StringUtils.isBlank(thirdpartyUser.getUserId())) {
                return setModelMap(modelMap,HttpCode.USER_NO_BOUND);
            }

            String userId = thirdpartyUser.getUserId();
            int thirdUserCount = (int) provider.execute(new Parameter("userThirdpartyService","selectCountByUserId").setId(userId)).getObject();
            if (thirdUserCount <= 1) {
                return setModelMap(modelMap,HttpCode.USER_NO_BOUND);
            }
            if (thirdpartyUser.getVerified()) {
                User user = (User) provider.execute(new Parameter("userService","queryById").setId(getCurrUser())).getModel();
                if (user != null && StringUtils.equals("2",user.getUserType())) {
                    user.setUserType("1");
                    provider.execute(new Parameter("userService","update").setModel(user));
                }
            }
            provider.execute(new Parameter("userThirdpartyService","delete").setId(thirdpartyUser.getId()));
            return setSuccessModelMap(modelMap);
        } else {

            thirdpartyUser = (UserThirdparty) provider.execute(new Parameter("userThirdpartyService","queryByThirdParty").setObjects(new Object[] {openId,type})).getModel();
            if (thirdpartyUser == null) {
                thirdpartyUser = new UserThirdparty();
                thirdpartyUser.setOpenId(openId);
                thirdpartyUser.setProvider(type);
                try {
                    ThirdPartyUser thirdUser = null;
                    if (StringUtils.equals(type.toUpperCase(),"WX")){
                        thirdUser = ThirdPartyLoginHelper.getWxUserinfo(token, openId);
                    }
                    if (StringUtils.equals(type.toUpperCase(),"QQ")){
                        thirdUser = ThirdPartyLoginHelper.getQQUserinfo(token, openId);
                    }
                    if (StringUtils.equals(type.toUpperCase(),"SINA")){
                        thirdUser = ThirdPartyLoginHelper.getSinaUserinfo(token, openId);
                        if (thirdUser.getVerified()) {

                            thirdpartyUser.setVerified(true);
                            User user = (User) provider.execute(new Parameter("userService","queryById").setId(getCurrUser())).getModel();
                            user.setUserType("2");
                            provider.execute(new Parameter("userService","update").setModel(user));
                        }
                    }

                    thirdpartyUser.setVerifiedReason(thirdUser.getVerifiedReason());

                    thirdpartyUser.setToken(token);
                    thirdpartyUser.setAvatar(thirdUser.getAvatarUrl());
                    thirdpartyUser.setName(thirdUser.getUserName());

                    thirdpartyUser.setToken(openId);

                    thirdpartyUser.setUserId(getCurrUser());


                    provider.execute(new Parameter("userThirdpartyService","update").setModel(thirdpartyUser));

                    return setSuccessModelMap(modelMap);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return setModelMap(modelMap,HttpCode.USER_HAS_BOUND);
            }

            return setSuccessModelMap(modelMap);
        }
    }

    // 绑定微信
    @ApiOperation(value = "手机号")
    @PostMapping(value = "bind/phone")
    public Object bindPhone(HttpServletRequest request, ModelMap modelMap,
                       @ApiParam(required = true, value = "手机号") @RequestParam(value = "phone") String phone,
                       @ApiParam(value = "密码") @RequestParam(value = "password",required = false) String password,
                       @ApiParam(required = true, value = "验证码") @RequestParam(value = "smsCode") int smsCode) {
        Map<String,Object> params = InstanceUtil.newHashMap();
        params.put("account",phone);

        Parameter parameter = new Parameter("userService","selectCheck").setMap(params);
        boolean phoneIsExist = (boolean) provider.execute(parameter).getObject();
        if (phoneIsExist) {
            return setModelMap(modelMap, HttpCode.REG_HAS_PHONE);
        }
        // 获取短信验证码
        JSONObject smsCaptcha = (JSONObject) CacheUtil.getCache().get((Constants.JR_SMS_CAPTCHA + phone));

        // 判断短信验证码是否失效
        Assert.containsKey(smsCaptcha, "captcha", "SMS_CAPTCHA_FAIL");

        int captcha = smsCaptcha.getInteger("captcha");
        // 判断验证码是否一致
        if (smsCode != captcha) {
            throw new IllegalArgumentException(Resources.getMessage(("SMS_CAPTCHA_INCORRECT")));
        }

        User record = (User) provider.execute(new Parameter("userService","queryById").setId(getCurrUser())).getModel();
        record.setAccount(phone);
        record.setPhone(phone);
        record.setPassword(password);

        provider.execute(new Parameter("userService","update").setModel(record));

        return setSuccessModelMap(modelMap);
    }

    // 绑定微信
    @ApiOperation(value = "手机号")
    @PostMapping(value = "change/phone")
    public Object changePhone(HttpServletRequest request, ModelMap modelMap,
                            @ApiParam(required = true, value = "手机号") @RequestParam(value = "phone") String phone,
                            @ApiParam(required = true, value = "验证码") @RequestParam(value = "smsCode") int smsCode) {

        User record = (User) provider.execute(new Parameter("userService","queryById").setId(getCurrUser())).getModel();

        if (!StringUtils.equals(phone,record.getPhone())) {
            return setModelMap(modelMap,HttpCode.UNKNOWN_PHONE);
        }

        Map<String,Object> params = InstanceUtil.newHashMap();
        params.put("account",phone);
        Parameter parameter = new Parameter("userService","selectCheck").setMap(params);
        boolean phoneIsExist = (boolean) provider.execute(parameter).getObject();
        if (phoneIsExist) {
            return setModelMap(modelMap, HttpCode.REG_HAS_PHONE);
        }

        // 获取短信验证码
        JSONObject smsCaptcha = (JSONObject) CacheUtil.getCache().get(("SMS_CAPTCHA:" + phone));

        // 判断短信验证码是否失效
//        Assert.containsKey(smsCaptcha, "captcha", "SMS_CAPTCHA_FAIL");

        int captcha = smsCaptcha.getInteger("captcha");
        // 判断验证码是否一致
        if (smsCode != captcha) {
            throw new IllegalArgumentException(Resources.getMessage(("SMS_CAPTCHA_INCORRECT")));
        }


        record.setAccount(phone);
        record.setPhone(phone);

        provider.execute(new Parameter("userService","update").setModel(record));

        return setSuccessModelMap(modelMap);
    }


    // 获取用户配置
    @ApiOperation(value = "获取用户配置")
    @GetMapping("/config")
    public Object config(HttpServletRequest request, ModelMap modelMap) {
        UserConfig record = (UserConfig) provider.execute(new Parameter("userConfigService","selectByUserId").setId(getCurrUser())).getModel();
        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        resultMap.put("isMessage",record.getIsMessage());
        resultMap.put("isWorks",record.getIsWorks());
        resultMap.put("isComment",record.getIsComment());
        resultMap.put("isMessageFollow",record.getIsMessageFollow());
        resultMap.put("isWorksFollow",record.getIsWorksFollow());
        resultMap.put("isCommentFollow",record.getIsCommentFollow());
        return setSuccessModelMap(modelMap,resultMap);
    }

    // 是否允许被私信
    @ApiOperation(value = "是否允许被私信")
    @GetMapping("/isAllowMessage")
    public Object isAllowMessage(HttpServletRequest request, ModelMap modelMap,
                 @ApiParam(value = "接收者用户ID") @RequestParam(value = "userId") String userId) {
        UserConfig record = (UserConfig) provider.execute(new Parameter("userConfigService","selectByUserId").setId(getCurrUser())).getModel();
        boolean flag = true;
        if (!record.getIsMessage()) {
            UserFollowing userFollowing = new UserFollowing();
            userFollowing.setFollowUserId(getCurrUser());
            userFollowing.setBefollowUserId(userId);
            flag = (boolean) provider.execute(new Parameter("userFollowingService","selectByIsFollow").setObjects(new Object[]{userId,getCurrUser()})).getObject();
        }
        Map<String,Object> dataMap = InstanceUtil.newHashMap();
        dataMap.put("isMessage",flag);
        return setSuccessModelMap(modelMap,dataMap);
    }


    // 用户配置修改
    @ApiOperation(value = "用户配置修改")
    @PostMapping("/config/modify")
    public Object configModify(HttpServletRequest request, ModelMap modelMap,
                       @ApiParam(value = "是否允许私信") @RequestParam(value = "isMessage") boolean isMessage,
                       @ApiParam(value = "是否允许改圖") @RequestParam(value = "isWorks") boolean isWorks,
                       @ApiParam(value = "是否允许评论") @RequestParam(value = "isComment") boolean isComment,
                       @ApiParam(value = "是否只允许關注的人私信") @RequestParam(value = "isMessageFollow") boolean isMessageFollow,
                       @ApiParam(value = "是否只允许關注的人改圖") @RequestParam(value = "isWorksFollow") boolean isWorksFollow,
                       @ApiParam(value = "是否只允许關注的人评论") @RequestParam(value = "isCommentFollow") boolean isCommentFollow) {
        UserConfig record = (UserConfig) provider.execute(new Parameter("userConfigService","selectByUserId").setId(getCurrUser())).getModel();
        record.setIsMessage(isMessage);
        record.setIsWorks(isWorks);
        record.setIsComment(isComment);
        record.setIsMessageFollow(isMessageFollow);
        record.setIsCommentFollow(isCommentFollow);
        record.setIsWorksFollow(isWorksFollow);
        provider.execute(new Parameter("userConfigService","update").setModel(record));
        return setSuccessModelMap(modelMap,"修改成功！");
    }


    // 消息通知
    @ApiOperation(value = "消息通知")
    @GetMapping("/remind")
    public Object remind(HttpServletRequest request, ModelMap modelMap) {
        String key = Constants.CACHE_NAMESPACE+Constants.CACHE_SHUO_NAMESPACE+"remind:"+getCurrUser();
        String comment = (String) CacheUtil.getCache().hget(key, PushType.NOTIFY_COMMENTS_NUM);
        String forward = (String) CacheUtil.getCache().hget(key, PushType.NOTIFY_FORWARD_NUM);
        String liked = (String) CacheUtil.getCache().hget(key, PushType.NOTIFY_LIKED_NUM);
        String AT = (String) CacheUtil.getCache().hget(key, PushType.NOTIFY_AT_NUM);

        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        resultMap.put("comment",StringUtils.isNotBlank(comment) ? Integer.parseInt(comment) : 0);
        resultMap.put("forward",StringUtils.isNotBlank(forward) ? Integer.parseInt(forward) : 0);
        resultMap.put("liked",StringUtils.isNotBlank(liked) ? Integer.parseInt(liked) : 0);
        resultMap.put("at",StringUtils.isNotBlank(AT) ? Integer.parseInt(AT) : 0);

        return setSuccessModelMap(modelMap,resultMap);
    }



}
