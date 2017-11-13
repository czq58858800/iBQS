package com.bq.shuo.web;

import com.bq.core.Constants;
import com.bq.core.config.Resources;
import com.bq.core.exception.LoginException;
import com.bq.core.support.HttpCode;
import com.bq.core.support.login.LoginHelper;
import com.bq.core.util.CacheUtil;
import com.bq.core.util.EncryptUtils;
import com.bq.core.util.InstanceUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.core.support.login.ThirdPartyLoginHelper;
import com.bq.shuo.core.support.login.ThirdPartyUser;
import com.bq.shuo.model.User;
import com.bq.shuo.model.UserThirdparty;
import com.bq.shuo.provider.IShuoProvider;
import com.bq.shuo.support.UserHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 第三方登录控制类
 * 
 * @author chern.zq
 * @version 2016年5月20日 下午3:12:56
 */
@Controller
@Api(value = "第三方登录接口", description = "第三方登录接口")
public class ThirdPartyLoginController extends AbstractController<IShuoProvider> {
	@Override
	public String getService() {
		return "userThirdpartyService";
	}

	@RequestMapping("/sns/wx")
	@ApiOperation(value = "微信登录", httpMethod = "POST")
	public Object wxCallback(HttpServletRequest request, ModelMap modelMap,
				 @RequestHeader(value = "Push-Device-Token",required = false) String pushDeviceToken,
				 @RequestHeader(value = "Login-Device",required = false) String LoginDevice,
				 @RequestHeader(value = "Login-lat",required = false) Double LoginLat,
				 @RequestHeader(value = "Login-lng",required = false) Double LoginLng,
				 @ApiParam(required = false, value = "name") @RequestParam(value = "name",required = false) String name,
				 @ApiParam(required = true, value = "Token") @RequestParam(value = "access_token") String access_token,
				 @ApiParam(required = true, value = "openId") @RequestParam(value = "openId") String openId) {
		// 获取第三方用户信息存放到session中
		try {

			ThirdPartyUser thirdUser = ThirdPartyLoginHelper.getWxUserinfo(access_token, openId);
			thirdUser.setProvider("WX");
			if (StringUtils.isNotBlank(name)) {
				thirdUser.setUserName(name);
			}
			if (!thirdPartyLogin(thirdUser)) {
				return setModelMap(modelMap, HttpCode.REG_HAS_NAME);
			}
			String token = EncryptUtils.encryptSHA256ToString(getCurrUser().toString()+System.currentTimeMillis());

			User record = (User) provider.execute(new Parameter("userService","queryById").setId(getCurrUser())).getModel();
			updateUser(record,token,pushDeviceToken,LoginDevice,LoginLat,LoginLng);
			return setSuccessModelMap(modelMap, UserHelper.formatLoginResultMap(token, record.getId(), record.getName(),record.getAvatar()));
		} catch (Exception e) {
			e.printStackTrace();

		}
		throw new LoginException(Resources.getMessage("LOGIN_FAIL"));
	}

	@RequestMapping("/sns/sina")
	@ApiOperation(value = "微博登录", httpMethod = "POST")
	public Object sinaCallback(HttpServletRequest request, ModelMap modelMap,
							   @RequestHeader(value = "Push-Device-Token",required = false) String pushDeviceToken,
							   @RequestHeader(value = "Login-Device",required = false) String LoginDevice,
							   @RequestHeader(value = "Login-lat",required = false) Double LoginLat,
							   @RequestHeader(value = "Login-lng",required = false) Double LoginLng,
							   @ApiParam(required = false, value = "name") @RequestParam(value = "name",required = false) String name,
							   @ApiParam(required = true, value = "Token") @RequestParam(value = "access_token") String access_token,
							   @ApiParam(required = true, value = "openId") @RequestParam(value = "openId") String openId) {
		String host = request.getHeader("host");
		try {
			// 获取第三方用户信息存放到session中
			ThirdPartyUser thirdUser = ThirdPartyLoginHelper.getSinaUserinfo(access_token,openId);
			thirdUser.setProvider("SINA");
			if (StringUtils.isNotBlank(name)) {
				thirdUser.setUserName(name);
			}
			if (!thirdPartyLogin(thirdUser)) {
				return setModelMap(modelMap,HttpCode.REG_HAS_NAME);
			}
			String token = EncryptUtils.encryptSHA256ToString(getCurrUser().toString()+System.currentTimeMillis());
			User record = (User) provider.execute(new Parameter("userService","queryById").setId(getCurrUser())).getModel();
			updateUser(record,token,pushDeviceToken,LoginDevice,LoginLat,LoginLng);
			return setSuccessModelMap(modelMap, UserHelper.formatLoginResultMap(token, record.getId(), record.getName(),record.getAvatar()));
		} catch (Exception e) {
			e.printStackTrace();

		}
		return setModelMap(modelMap,HttpCode.LOGIN_FAIL,"第三方授权失败");
	}

	@RequestMapping("/sns/qq")
	@ApiOperation(value = "QQ登录", httpMethod = "POST")
	public Object qqCallback(HttpServletRequest request, ModelMap modelMap,
				 @RequestHeader(value = "Push-Device-Token",required = false) String pushDeviceToken,
				 @RequestHeader(value = "Login-Device",required = true) String LoginDevice,
				 @RequestHeader(value = "Login-lat",required = false) Double LoginLat,
				 @RequestHeader(value = "Login-lng",required = false) Double LoginLng,
				 @ApiParam(required = false, value = "name") @RequestParam(value = "name",required = false) String name,
				 @ApiParam(required = true, value = "Token") @RequestParam(value = "access_token") String access_token,
				 @ApiParam(required = true, value = "openId") @RequestParam(value = "openId") String openId) {
		try {
			// 获取第三方用户信息存放到session中
			ThirdPartyUser thirdUser = ThirdPartyLoginHelper.getQQUserinfo(LoginDevice,access_token, openId);
			thirdUser.setProvider("QQ");
			if (StringUtils.isNotBlank(name)) {
				thirdUser.setUserName(name);
			}
			if (!thirdPartyLogin(thirdUser)) {
				return setModelMap(modelMap,HttpCode.REG_HAS_NAME);
			}
			String token = EncryptUtils.encryptSHA256ToString(getCurrUser().toString()+System.currentTimeMillis());
			User record = (User) provider.execute(new Parameter("userService","queryById").setId(getCurrUser())).getModel();
			updateUser(record,token,pushDeviceToken,LoginDevice,LoginLat,LoginLng);
			return setSuccessModelMap(modelMap, UserHelper.formatLoginResultMap(token, record.getId(), record.getName(),record.getAvatar()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return setModelMap(modelMap,HttpCode.LOGIN_FAIL);
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
		provider.execute(new Parameter("userService","update").setModel(record));
	}

	public boolean thirdPartyLogin(ThirdPartyUser thirdUser) {
		User user = null;
		// 查询是否已经绑定过
		Parameter parameter = new Parameter(getService(),"queryByThirdParty").setObjects(new Object[] {thirdUser.getOpenid(),thirdUser.getProvider()});
		UserThirdparty thirdpartyUser = (UserThirdparty) provider.execute(parameter).getModel();
		if (thirdpartyUser == null) {
			Map<String,Object> params = InstanceUtil.newHashMap();
			params.put("name",thirdUser.getUserName());
			parameter = new Parameter("userService","selectCheck").setMap(params);
			boolean nicknameIsExist = (boolean) provider.execute(parameter).getObject();
			if (nicknameIsExist) return false;
			parameter = new Parameter(getService(),"insertThirdPartyUser").setModel(thirdUser);
			user = (User) provider.execute(parameter).getModel();
		} else {
			thirdpartyUser.setToken(thirdUser.getToken());
			thirdpartyUser.setVerified(thirdUser.getVerified());
			thirdpartyUser.setVerifiedReason(thirdUser.getVerifiedReason());

			parameter = new Parameter(getService(),"update").setModel(thirdpartyUser);
			provider.execute(parameter);

			parameter = new Parameter("userService","queryById").setId(thirdpartyUser.getUserId());
			user = (User) provider.execute(parameter).getModel();
			if (!StringUtils.equals(user.getUserType(),thirdUser.getVerified() ? "2" : "1") && !StringUtils.equals("3",user.getUserType())) {
				user.setUserType("2");
				user.setVerifiedReason(thirdUser.getVerifiedReason());
				parameter = new Parameter("userService","update").setModel(user);
				provider.execute(parameter).getModel();
			}
		}
		if (StringUtils.isNotBlank(user.getPassword())) {
			LoginHelper.login(user.getAccount(), user.getPassword());
		} else {
			LoginHelper.login(user.getAccount());
		}
		return true;
	}
}
