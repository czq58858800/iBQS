package com.bq.shuo.core.shiro;

import com.bq.core.util.WebUtil;
import com.bq.shuo.core.base.BaseProvider;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
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
 * 权限检查类
 * 
 * @author Harvey.Wei
 * @version 2016年5月20日 下午3:44:45
 */
public class Realm extends AuthorizingRealm {
    private final Logger logger = LogManager.getLogger();
    @Autowired
    @Qualifier("shuoProvider")
    protected BaseProvider provider;
    @Autowired
    private RedisOperationsSessionRepository sessionRepository;

    // 权限
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        String userId = (String) WebUtil.getCurrentUser();
//        Parameter parameter = new Parameter("sysAuthorizeService", "queryPermissionByUserId").setId(userId);
//        logger.info("{} execute queryPermissionByUserId start...", parameter.getNo());
//        List<?> list = provider.execute(parameter).getList();
//        logger.info("{} execute queryPermissionByUserId end.", parameter.getNo());
//        for (Object permission : list) {
//            if (StringUtils.isNotBlank((String)permission)) {
//                // 添加基于Permission的权限信息
//                info.addStringPermission((String)permission);
//            }
//        }
//        // 添加用户权限
        info.addStringPermission("user");
        return info;
    }

    // 登录验证
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
        throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)authcToken;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("enable", 1);
        params.put("account", token.getUsername());
        Parameter parameter = new Parameter("userService", "queryList").setMap(params);
        logger.info("{} execute userService.queryList start...", parameter.getNo());
        List<?> list = provider.execute(parameter).getList();
        logger.info("{} execute userService.queryList end.", parameter.getNo());
        if (list.size() == 1) {
            User user = (User) list.get(0);
            StringBuilder sb = new StringBuilder(100);
            for (int i = 0; i < token.getPassword().length; i++) {
                sb.append(token.getPassword()[i]);
            }
            if (user.getPassword().equals(sb.toString())) {
                WebUtil.saveCurrentUser(user.getId());
                saveSession(user.getAccount());
                AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(user.getAccount(), user.getPassword(),
                    user.getName());
                return authcInfo;
            }
            logger.warn("USER [{}] PASSWORD IS WRONG: {}", token.getUsername(), sb.toString());
            return null;
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
        Parameter parameter = new Parameter("sessionService", "querySessionIdByAccount").setModel(record);
        logger.info("{} execute querySessionIdByAccount start...", parameter.getNo());
        List<?> sessionIds = provider.execute(parameter).getList();
        logger.info("{} execute querySessionIdByAccount end.", parameter.getNo());
        if (sessionIds != null) {
            for (Object sessionId : sessionIds) {
                record.setSessionId((String)sessionId);
                parameter = new Parameter("sessionService", "deleteBySessionId").setModel(record);
                logger.info("{} execute deleteBySessionId start...", parameter.getNo());
                provider.execute(parameter);
                logger.info("{} execute deleteBySessionId end.", parameter.getNo());
                sessionRepository.delete((String)sessionId);
                sessionRepository.cleanupExpiredSessions();
            }
        }
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        record.setSessionId(session.getId().toString());
        String host = (String)session.getAttribute("HOST");
        record.setIp(StringUtils.isBlank(host) ? session.getHost() : host);
        record.setStartTime(session.getStartTimestamp());
        parameter = new Parameter("sessionService", "update").setModel(record);
        logger.info("{} execute sessionService.update start...", parameter.getNo());
        provider.execute(parameter);
        logger.info("{} execute sessionService.update end.", parameter.getNo());
    }
}
