package com.bq.shuo.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.config.Resources;
import com.bq.core.exception.LoginException;
import com.bq.core.support.Assert;
import com.bq.core.support.HttpCode;
import com.bq.core.support.login.LoginHelper;
import com.bq.core.util.EncryptUtils;
import com.bq.core.util.InstanceUtil;
import com.bq.model.Login;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.support.UserHelper;
import com.bq.shuo.model.User;
import com.bq.shuo.provider.IShuoProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import static org.apache.shiro.web.filter.mgt.DefaultFilter.user;

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
        SecurityUtils.getSubject().logout();
        Map<String, Object> params = InstanceUtil.newHashMap();
        params.put("token", token);
        Parameter parameter = new Parameter(getService(),"query").setMap(params);
        Page<User> userPage = (Page<User>) provider.execute(parameter).getPage();
        if (userPage != null && userPage.getRecords().size() > 0) {
            User user = userPage.getRecords().get(0);
            // 用户名密码登录
            if (LoginHelper.login(user.getAccount())) {
                String tokenId = EncryptUtils.encryptSHA256ToString(getCurrUser().toString()+System.currentTimeMillis());
                User record = (User) provider.execute(new Parameter(getService(),"queryById").setId(getCurrUser())).getModel();
                updateUser(record,token,pushDeviceToken,LoginDevice,LoginLat,LoginLng);
                return setSuccessModelMap(modelMap, UserHelper.formatLoginResultMap(tokenId, record.getId(), record.getName(),record.getAvatar()));
            }
        }
        throw new LoginException(Resources.getMessage("TOKEN_INVALID"));
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
        if (StringUtils.isNotBlank(token)) {
            record.setToken(token);
        }
        provider.execute(new Parameter(getService(),"update").setModel(record));
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
