package com.bq.core.support.login;

import com.bq.core.exception.LoginException;
import com.bq.core.config.Resources;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

/**
 * @author Harvey.Wei
 * @version 2016年5月20日 下午3:44:45
 */
public final class LoginHelper {
	private LoginHelper() {
	}

	/** 用户登录 */
	public static final Boolean login(String account, String password) {
		UsernamePasswordToken token = new UsernamePasswordToken(account, password);
		token.setRememberMe(true);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			return subject.isAuthenticated();
		} catch (LockedAccountException e) {
			throw new LoginException(Resources.getMessage("ACCOUNT_LOCKED", token.getPrincipal()));
		} catch (DisabledAccountException e) {
			throw new LoginException(Resources.getMessage("ACCOUNT_DISABLED", token.getPrincipal()));
		} catch (ExpiredCredentialsException e) {
			throw new LoginException(Resources.getMessage("ACCOUNT_EXPIRED", token.getPrincipal()));
		} catch (Exception e) {
			throw new LoginException(Resources.getMessage("LOGIN_FAIL"), e);
		}
	}

	/** 无密码用户登录 */
	public static final Boolean login(String account) {
		NoPwdAuthenticationToken token = new NoPwdAuthenticationToken(account);

		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			return subject.isAuthenticated();
		} catch (LockedAccountException e) {
			throw new LoginException(Resources.getMessage("ACCOUNT_LOCKED", token.getPrincipal()));
		} catch (DisabledAccountException e) {
			throw new LoginException(Resources.getMessage("ACCOUNT_DISABLED", token.getPrincipal()));
		} catch (ExpiredCredentialsException e) {
			throw new LoginException(Resources.getMessage("ACCOUNT_EXPIRED", token.getPrincipal()));
		} catch (Exception e) {
			throw new LoginException(Resources.getMessage("LOGIN_FAIL"), e);
		}
	}
}
