package com.bq.shuo.web;

import com.alibaba.fastjson.JSONObject;
import com.bq.core.Constants;
import com.bq.core.captcha.VerifyCodeUtils;
import com.bq.core.config.Resources;
import com.bq.core.exception.LoginException;
import com.bq.core.support.Assert;
import com.bq.core.support.HttpCode;
import com.bq.core.support.login.LoginHelper;
import com.bq.core.util.*;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.core.util.SendSms;
import com.bq.shuo.model.User;
import com.bq.shuo.provider.IShuoProvider;
import com.bq.shuo.support.UserHelper;
import com.qcloud.sms.SmsSingleSenderResult;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Random;

/**
 * 用户登录
 * 
 * @author Harvey.Wei
 * @version 2016年5月20日 下午3:11:21
 */
@RestController
@Api(value = "登录接口", description = "登录接口")
public class LoginController extends AbstractController<IShuoProvider> {

    public String getService() {
        return "userService";
    }

    // 登录
    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public Object login(
            @RequestHeader(value = "Push-Device-Token",required = false) String pushDeviceToken,
            @RequestHeader(value = "Login-Device",required = false) String LoginDevice,
            @RequestHeader(value = "Login-lat",required = false) Double LoginLat,
            @RequestHeader(value = "Login-lng",required = false) Double LoginLng,
            @ApiParam(required = true, value = "登录帐号") @RequestParam(value = "account") String account,
            @ApiParam(required = true, value = "登录密码") @RequestParam(value = "password") String password, ModelMap modelMap,
                        HttpServletRequest request) {
        Assert.notNull(account, "ACCOUNT");
        Assert.notNull(account, "PASSWORD");
        if (LoginHelper.login(account, password)) {
            String token = EncryptUtils.encryptSHA256ToString(getCurrUser().toString()+System.currentTimeMillis());
            User record = (User) provider.execute(new Parameter(getService(),"queryById").setId(getCurrUser())).getModel();
            updateUser(record,token,pushDeviceToken,LoginDevice,LoginLat,LoginLng);
            request.setAttribute("msg", "[" + account + "]登录成功.");
            return setSuccessModelMap(modelMap, UserHelper.formatLoginResultMap(token, record.getId(), record.getName(),record.getAvatar()));
        }
        request.setAttribute("msg", "[" + account + "]登录失败.");
        throw new LoginException(Resources.getMessage("LOGIN_FAIL"));
    }

    // 登录
    @ApiOperation(value = "用户登录")
    @PostMapping("/tokenLogin")
    public Object tokenLogin(HttpServletRequest request,ModelMap modelMap,
                             @RequestHeader(value = "Push-Device-Token",required = false) String pushDeviceToken,
                             @RequestHeader(value = "Login-Device",required = false) String LoginDevice,
                             @RequestHeader(value = "Login-lat",required = false) Double LoginLat,
                             @RequestHeader(value = "Login-lng",required = false) Double LoginLng,
                             @ApiParam(required = true, value = "Token") @RequestParam(value = "token") String token) {

        String cacheKey = Constants.CACHE_NAMESPACE+Constants.CACHE_SHUO_NAMESPACE+"LOGIN_TOKEN:"+token;
        if (CacheUtil.getCache().exists(cacheKey)) {
            String userId = (String) CacheUtil.getCache().get(cacheKey);
            Parameter parameter = new Parameter(getService(),"queryById").setId(userId);
            User record = (User) provider.execute(parameter).getModel();
            if (record != null && StringUtils.isNotBlank(record.getId())) {
                if (StringUtils.isNotBlank(getCurrUser())) {
                    CacheUtil.getCache().expire(cacheKey,60*60*24*7);
                    return setSuccessModelMap(modelMap, UserHelper.formatLoginResultMap(token, record.getId(), record.getName(),record.getAvatar()));
                }

                if (LoginHelper.login(record.getAccount())) {
                    CacheUtil.getCache().del(cacheKey);
                    String tokenId = EncryptUtils.encryptSHA256ToString(getCurrUser().toString()+System.currentTimeMillis());
                    updateUser(record,tokenId,pushDeviceToken,LoginDevice,LoginLat,LoginLng);
                    return setSuccessModelMap(modelMap, UserHelper.formatLoginResultMap(tokenId, record.getId(), record.getName(),record.getAvatar()));
                }
            }
        }

        throw new LoginException(Resources.getMessage("TOKEN_INVALID"));
    }

    // 发送SMS验证码
    @ApiOperation(value = "发送SMS验证码")
    @GetMapping("/sendSMS")
    public Object sendSMS(ModelMap modelMap,
              @ApiParam(required = true, value = "帐号") @RequestParam(value = "account") String account,
              @ApiParam(required = true, value = "类型：(Y:需要注册;N:不需要)") @RequestParam(value = "t",required = false) String t) throws UnsupportedEncodingException {
        // 账号不允许为空
        Assert.notNull(account,"USER_ID_IS_NULL");

        if (StringUtils.isNotBlank(t) && StringUtils.equals(t.trim().toUpperCase(),"Y")) {
            Parameter parameter = new Parameter("userService","selectByPhone").setId(account);
            User user = (User) provider.execute(parameter).getModel();
            if (user == null) {
                return setModelMap(modelMap,HttpCode.USER_ACCOUNT_NOT_EXIST);
            }
        }

        String lockKey = new StringBuilder(Constants.JR_SMS_CAPTCHA).append("LOCK:").append(account).toString();

        while (!CacheUtil.getLock(lockKey)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }

        JSONObject res = new JSONObject();

        try {
            int captcha;
            Random ne=new Random();//实例化一个random的对象ne
            captcha=ne.nextInt(9999-1000+1)+1000;//为变量赋随机值1000-9999

            res.put("account",account);
            res.put("captcha",captcha);
            res.put("timestamp", System.currentTimeMillis());

            SmsSingleSenderResult sendStatus = SendSms.sendVerifyCode(account,captcha);

            if (sendStatus == null) {
                return setModelMap(modelMap,HttpCode.UNKNOWN_PHONE);
            }

            if (sendStatus.result != 0) {
                modelMap.put("code", sendStatus.result);
                modelMap.put("msg", sendStatus.errMsg);
                modelMap.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.ok(modelMap);
            }

            // 序列化验证码信息到Redis
            CacheUtil.getCache().set(Constants.JR_SMS_CAPTCHA+account,res);
            logger.debug(res.toString());

            return setModelMap(modelMap,HttpCode.OK);
        } finally {
            CacheUtil.unlock(lockKey);
        }
    }



    // 发送SMS验证码
    @ApiOperation(value = "获取验证码")
    @GetMapping("/getImgCaptcha")
    public Object getCaptcha(ModelMap modelMap,
                 HttpServletRequest request,
                 HttpServletResponse response,
                 @ApiParam(value = "验证码宽度") @RequestParam(value = "w",required = false) Integer w,
                 @ApiParam(value = "验证码高度") @RequestParam(value = "h",required = false) Integer h,
                 @ApiParam(required = true, value = "帐号") @RequestParam(value = "account") String account) throws UnsupportedEncodingException {
        // 账号不允许为空
        Assert.notNull(account,"USER_ID_IS_NULL");

        String lockKey = new StringBuilder(Constants.JR_IMG_CAPTCHA).append("LOCK:").append(account).toString();

        while (!CacheUtil.getLock(lockKey)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }

        JSONObject res = new JSONObject();

        try {
            String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
            res.put("account",account);
            res.put("captcha",verifyCode);
            res.put("timestamp", System.currentTimeMillis());
            // 序列化验证码信息到Redis
            CacheUtil.getCache().set(Constants.JR_IMG_CAPTCHA+account,res);
            logger.debug(res.toString());

//            try {
//                response.setHeader("Pragma", "No-cache");
//                response.setHeader("Cache-Control", "no-cache");
//                response.setDateHeader("Expires", 0);
//                response.setContentType("image/jpeg");
//                VerifyCodeUtils.outputImage(w == null ? 276 : w,h == null ? 114 : h,response.getOutputStream(),verifyCode);
//                return ResponseEntity.ok();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            return setSuccessModelMap(modelMap,VerifyCodeUtils.encodeImgageToBase64(w == null ? 276 : w,h == null ? 114 : h,verifyCode));

        } finally {
            CacheUtil.unlock(lockKey);
        }
    }

    // 校验帐号是否存在
    @ApiOperation(value = "验证码是否正确")
    @GetMapping("/check/captcha")
    public Object checkCaptcha(ModelMap modelMap,
                   @ApiParam(required = true, value = "手机号") @RequestParam(value = "account") String account,
                   @ApiParam(required = true, value = "验证码") @RequestParam(value = "captcha") String captcha) throws UnsupportedEncodingException {
        // 获取短信验证码
        JSONObject jCaptcha = (JSONObject) CacheUtil.getCache().get((Constants.JR_IMG_CAPTCHA+account));
        // 判断短信验证码是否失效
        if (jCaptcha == null) {
            return setModelMap(modelMap,HttpCode.SMS_CAPTCHA_FAIL);
        }
        String imgCaptcha = jCaptcha.getString("captcha");
        // 判断验证码是否一致
        if (!StringUtils.equals(imgCaptcha.trim().toLowerCase(),captcha.trim().toLowerCase())) {
            CacheUtil.getCache().del(Constants.JR_IMG_CAPTCHA+account);
            return setModelMap(modelMap,HttpCode.SMS_CAPTCHA_INCORRECT);
        }
        return setSuccessModelMap(modelMap);
    }

    // 校验帐号是否存在
    @ApiOperation(value = "验证码是否正确")
    @GetMapping("/check/smsCaptcha")
    public Object checkSMSCaptcha(ModelMap modelMap,
                               @ApiParam(required = true, value = "手机号") @RequestParam(value = "account") String account,
                               @ApiParam(required = true, value = "验证码") @RequestParam(value = "captcha") int captcha) throws UnsupportedEncodingException {
        // 获取短信验证码
        JSONObject jCaptcha = (JSONObject) CacheUtil.getCache().get((Constants.JR_SMS_CAPTCHA+account));
        // 判断短信验证码是否失效
        if (jCaptcha == null) {
            return setModelMap(modelMap,HttpCode.SMS_CAPTCHA_FAIL);
        }
        int smsCaptcha = jCaptcha.getInteger("captcha");
        // 判断验证码是否一致
        if (smsCaptcha != captcha) {
            return setModelMap(modelMap,HttpCode.SMS_CAPTCHA_INCORRECT);
        }
        return setSuccessModelMap(modelMap);
    }

    // 校验帐号是否存在
    @ApiOperation(value = "校验帐号是否存在")
    @GetMapping("/check/account")
    public Object checkAccount(ModelMap modelMap,
                               @ApiParam(required = true, value = "验证值") @RequestParam(value = "account") String account) throws UnsupportedEncodingException {
        Map<String, Object> params = InstanceUtil.newHashMap();
        params.put("account", account);

        Parameter selectCheckAccountParam = new Parameter(getService(),"selectCheck").setMap(params);
        boolean isAccountExist = (boolean) provider.execute(selectCheckAccountParam).getObject();
        if (isAccountExist) {
            return setModelMap(modelMap, HttpCode.REG_HAS_PHONE);
        }
        return setSuccessModelMap(modelMap);
    }

    // 校验昵称是否存在
    @ApiOperation(value = "校验昵称是否存在")
    @GetMapping("/check/nickname")
    public Object checkNickname(ModelMap modelMap,
                                @ApiParam(required = true, value = "验证值") @RequestParam(value = "nickname") String nickname) throws UnsupportedEncodingException {
        Map<String, Object> params = InstanceUtil.newHashMap();
        params.put("name", nickname.trim());

        Parameter selectCheckParam = new Parameter(getService(),"selectCheck").setMap(params);
        boolean isNicknameExist = (boolean) provider.execute(selectCheckParam).getObject();
        if (isNicknameExist) {
            return setModelMap(modelMap, HttpCode.REG_HAS_NAME);
        }
        return setSuccessModelMap(modelMap);
    }

    private void updateUser(User record, String token,String pushDeviceToken, String loginDevice, Double loginLat, Double loginLng) {
        if (loginLat != null && loginLng != null) {
            record.setLat(loginLat);
            record.setLng(loginLng);
        }
        if (StringUtils.isNotBlank(loginDevice)) {
            record.setLoginDevice(loginDevice);
        }
        if (StringUtils.isNotBlank(pushDeviceToken)) {
            record.setPushDeviceToken(pushDeviceToken);
        }
        String cacheKey = Constants.CACHE_NAMESPACE+Constants.CACHE_SHUO_NAMESPACE+"LOGIN_TOKEN:"+token;
        CacheUtil.getCache().set(cacheKey,record.getId());
        CacheUtil.getCache().expire(cacheKey,60*60*24*7);
        provider.execute(new Parameter(getService(),"update").setModel(record));
    }

    // 注册
    @ApiOperation(value = "用户注册")
    @PostMapping("/regin")
    public Object regin(HttpServletRequest request, ModelMap modelMap,
                        @RequestHeader(value = "Push-Device-Token",required = false) String pushDeviceToken,
                        @RequestHeader(value = "Login-Device",required = false) String LoginDevice,
                        @RequestHeader(value = "Login-lat",required = false) Double LoginLat,
                        @RequestHeader(value = "Login-lng",required = false) Double LoginLng,
                        @ApiParam(required = true, value = "帐号") @RequestParam(value = "account") String account,
                        @ApiParam(required = true, value = "头像") @RequestParam(value = "avatar") String avatar,
                        @ApiParam(required = true, value = "验证码") @RequestParam(value = "smsCode") int smsCode,
                        @ApiParam(required = true, value = "昵称") @RequestParam(value = "name") String name,
                        @ApiParam(required = true, value = "密码") @RequestParam(value = "password") String password,
                        @ApiParam(required = false, value = "简介") @RequestParam(value = "summary",required = false) String summary,
                        @ApiParam(required = true, value = "性别") @RequestParam(value = "sex") int sex) {
        Map<String,Object> params = WebUtil.getParameterMap(request);

        User user = new User(account,name,password,summary,sex,pushDeviceToken,LoginDevice,LoginLat,LoginLng);
        user.setAvatar(avatar);
        // 账号不允许为空
        Assert.notNull(account,"ACCOUNT");
        // 验证码不允许为空
        Assert.notNull(smsCode,"SMS_CODE");
        Assert.notNull(name,"NAME");
        Assert.notNull(name,"SUMMARY");

        // 判断手机号是否注册
        params.clear();
        params.put("account",account);
        Parameter parameter = new Parameter(getService(),"selectCheck").setMap(params);
        boolean isAccountExist = (boolean) provider.execute(parameter).getObject();
        if (isAccountExist) {
            return setModelMap(modelMap, HttpCode.REG_HAS_PHONE);
        }
        // 判断昵称是否注册
        params.clear();
        params.put("name",name);
        parameter = new Parameter(getService(),"selectCheck").setMap(params);
        boolean isNicknameExist = (boolean) provider.execute(parameter).getObject();
        if (isNicknameExist) {
            return setModelMap(modelMap, HttpCode.REG_HAS_NAME);
        }

        // 获取短信验证码
        JSONObject jCaptcha = (JSONObject) CacheUtil.getCache().get((Constants.JR_SMS_CAPTCHA+account));
        // 判断短信验证码是否失效
        if (jCaptcha == null) {
            return setModelMap(modelMap,HttpCode.SMS_CAPTCHA_FAIL);
        }


        int captcha = jCaptcha.getInteger("captcha");
        // 判断验证码是否一致
        if (smsCode != captcha) {
            return setModelMap(modelMap,HttpCode.SMS_CAPTCHA_INCORRECT);
        }

        // 注册用户
        parameter = new Parameter("userService","update").setModel(user);
        user = (User) provider.execute(parameter).getModel();
        CacheUtil.getCache().del(Constants.JR_SMS_CAPTCHA+account);

        String domain = PropertiesUtil.getString("qiniu.domain");
        Auth auth = Auth.create(PropertiesUtil.getString("qiniu.access_key"), PropertiesUtil.getString("qiniu.secret_key"));
        String bucket = PropertiesUtil.getString("qiniu.bucket");
        Configuration cfg = new Configuration(Zone.zone0());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        String key = avatar.replace(domain, "");
        String newKey = "avatar/"+DateUtil.getDateTime(DateUtil.DATE_PATTERN.YYYYMMDD)+"/"+DateUtil.getDateTime(DateUtil.DATE_PATTERN.HHMMSS)+user.getId()+key.substring(key.lastIndexOf("."),key.length());
        try {
            bucketManager.rename(bucket,key,newKey);
        } catch (QiniuException e) {
            e.printStackTrace();
        }


        // 有密码
        if (StringUtils.isNotEmpty(user.getPassword())) {
            if (LoginHelper.login(account, password)) {
                String token = EncryptUtils.encryptSHA256ToString(getCurrUser().toString()+System.currentTimeMillis());
                user.setAvatar(domain+newKey);
                updateUser(user,token,pushDeviceToken,LoginDevice,LoginLat,LoginLng);
                request.setAttribute("msg", "[" + account + "]登录成功.");
                return setSuccessModelMap(modelMap, UserHelper.formatLoginResultMap(token, user.getId(), user.getName(),user.getAvatar()));
            }
        } else {
            if (LoginHelper.login(user.getAccount())) {
                String tokenId = EncryptUtils.encryptSHA256ToString(getCurrUser().toString()+System.currentTimeMillis());
                user.setAvatar(domain+newKey);
                updateUser(user,tokenId,pushDeviceToken,LoginDevice,LoginLat,LoginLng);
                return setSuccessModelMap(modelMap, UserHelper.formatLoginResultMap(tokenId, user.getId(), user.getName(),user.getAvatar()));
            }
        }

        throw new IllegalArgumentException(Resources.getMessage("REGISTER_FAIL"));
    }

    // 忘记密码
    @ApiOperation(value = "忘记密码")
    @PostMapping("/forget")
    public Object forget(HttpServletRequest request, ModelMap modelMap,
                         @RequestHeader(value = "Push-Device-Token",required = false) String pushDeviceToken,
                         @RequestHeader(value = "Login-Device",required = false) String LoginDevice,
                         @RequestHeader(value = "Login-lat",required = false) Double LoginLat,
                         @RequestHeader(value = "Login-lng",required = false) Double LoginLng,
                        @ApiParam(required = true, value = "帐号") @RequestParam(value = "account") String account,
                        @ApiParam(required = true, value = "验证码") @RequestParam(value = "smsCode") int smsCode,
                        @ApiParam(required = true, value = "密码") @RequestParam(value = "password") String password) {
        // 账号不允许为空
        Assert.notNull(account,"ACCOUNT");
        // 验证码不允许为空
        Assert.notNull(smsCode,"SMS_CODE");
        Assert.notNull(password,"PASSWORD");

        // 获取短信验证码
        JSONObject jCaptcha = (JSONObject) CacheUtil.getCache().get((Constants.JR_SMS_CAPTCHA+account));
        // 判断短信验证码是否失效
        if (jCaptcha == null) {
            return setModelMap(modelMap,HttpCode.SMS_CAPTCHA_FAIL);
        }

        int captcha = jCaptcha.getInteger("captcha");
        // 判断验证码是否一致
        if (smsCode != captcha) {
            return setModelMap(modelMap,HttpCode.SMS_CAPTCHA_INCORRECT);
        }

        Parameter parameter = new Parameter("userService","selectByPhone").setId(account);
        User user = (User) provider.execute(parameter).getModel();
        if (user == null) {
            return setModelMap(modelMap,HttpCode.USER_ACCOUNT_NOT_EXIST);
        }
        user.setPassword(password);
        parameter = new Parameter("userService","update").setModel(user);
        provider.execute(parameter);

        if (LoginHelper.login(user.getAccount(),password)) {
            String tokenId = EncryptUtils.encryptSHA256ToString(getCurrUser().toString()+System.currentTimeMillis());
            User record = (User) provider.execute(new Parameter(getService(),"queryById").setId(getCurrUser())).getModel();
            updateUser(record,tokenId,pushDeviceToken,LoginDevice,LoginLat,LoginLng);
            return setSuccessModelMap(modelMap, UserHelper.formatLoginResultMap(tokenId, record.getId(), record.getName(),record.getAvatar()));
        }

        throw new IllegalArgumentException(Resources.getMessage(("SMS_CAPTCHA_INCORRECT")));
    }




    // 登出
    @ApiOperation(value = "用户登出")
    @PostMapping("/logout")
    public Object logout(ModelMap modelMap) {
        SecurityUtils.getSubject().logout();
        return setSuccessModelMap(modelMap);
    }
    // 没有登录
    @ApiOperation(value = "没有登录")
    @RequestMapping(value = "/unauthorized", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
    public Object unauthorized(ModelMap modelMap) throws Exception {
        return setModelMap(modelMap, HttpCode.UNAUTHORIZED);
    }

    // 没有权限
    @ApiOperation(value = "没有权限")
    @RequestMapping(value = "/forbidden", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
    public Object forbidden(ModelMap modelMap) {
        return setModelMap(modelMap, HttpCode.FORBIDDEN);
    }
}
