package com.bq.shuo.core.shiro;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.support.login.NoPwdAuthenticationToken;
import com.bq.core.util.WebUtil;
import com.bq.shuo.core.base.BaseProvider;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Harvey.Wei
 * @version 2016/8/23
 */
public class AuthRealm extends AuthorizingRealm {
    private final Logger logger = LogManager.getLogger();
    @Autowired
    @Qualifier("shuoProvider")
    protected BaseProvider provider;
    @Autowired
    private RedisOperationsSessionRepository sessionRepository;

    public AuthRealm(){
        // 设置无需凭证，因为从sso认证后才会有用户名
        setCredentialsMatcher(new AllowAllCredentialsMatcher());
        // 设置token为我们自定义的
        setAuthenticationTokenClass(NoPwdAuthenticationToken.class);
    }

    // 权限
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        String userId = WebUtil.getCurrentUser();
        // 添加用户权限
//        info.addStringPermission("user");
        return info;
    }

    // 登录验证
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
            throws AuthenticationException {
        NoPwdAuthenticationToken token = (NoPwdAuthenticationToken) authcToken;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("enable", 1);
        params.put("account", token.getUsername());
        Parameter parameter = new Parameter("userService", "queryList").setMap(params);
        logger.info("{} execute userService.queryList start...", parameter.getNo());
        List<?> list = provider.execute(parameter).getList();
        logger.info("{} execute userService.queryList end.", parameter.getNo());
        if (list.size() == 1) {
            User user = (User) list.get(0);

            WebUtil.saveCurrentUser(user.getId());
            saveSession(user.getAccount());

            AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(user.getAccount(), user.getPassword(),
                    user.getName());
            return authcInfo;
        } else {
            logger.warn("No user: {}", token.getUsername());
            return null;
        }
    }

    /** 保存session */
    private void saveSession(String account) {
        // 踢出用户
        com.bq.shuo.model.Session record = new com.bq.shuo.model.Session();
        record.setAccount(account);
//        Parameter parameter = new Parameter("sessionService", "querySessionIdByAccount").setModel(record);
//        logger.info("{} execute querySessionIdByAccount start...", parameter.getNo());
//        List<?> sessionIds = provider.execute(parameter).getList();
//        logger.info("{} execute querySessionIdByAccount end.", parameter.getNo());
//        if (sessionIds != null) {
//            for (Object sessionId : sessionIds) {
//                record.setSessionId((String)sessionId);
//                parameter = new Parameter("sessionService", "deleteBySessionId").setModel(record);
//                logger.info("{} execute deleteBySessionId start...", parameter.getNo());
//                provider.execute(parameter);
//                logger.info("{} execute deleteBySessionId end.", parameter.getNo());
//                sessionRepository.delete((String)sessionId);
//                sessionRepository.cleanupExpiredSessions();
//            }
//        }
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        record.setSessionId(session.getId().toString());
        String host = (String)session.getAttribute("HOST");
        record.setIp(StringUtils.isBlank(host) ? session.getHost() : host);
        record.setStartTime(session.getStartTimestamp());
        Parameter parameter = new Parameter("sessionService", "update").setModel(record);
        logger.info("{} execute sessionService.update start...", parameter.getNo());
        provider.execute(parameter);
        logger.info("{} execute sessionService.update end.", parameter.getNo());
    }


}
