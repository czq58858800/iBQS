package com.bq.shuo.service;

import com.bq.core.Constants;
import com.bq.core.util.CacheUtil;
import com.bq.core.util.InstanceUtil;
import com.bq.core.util.PropertiesUtil;
import com.bq.shuo.mapper.SessionMapper;
import com.bq.shuo.model.Session;
import com.bq.shuo.core.base.BaseService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 会话管理  服务实现类
 * </p>
 *
 * @author chern.zq
 * @since 2017-04-13
 */
@Service
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"session")
public class SessionService extends BaseService<Session> {

    @CachePut
    @Transactional
    public Session update(Session record) {
        if (record.getId() == null) {
            record.setUpdateTime(new Date());
            String id = ((SessionMapper) mapper).queryBySessionId(record.getSessionId());
            if (id != null) {
                mapper.updateById(record);
            } else {
                record.setCreateTime(new Date());
                mapper.insert(record);
            }
        } else {
            mapper.updateById(record);
        }
        return record;
    }

    // 系统触发,由系统自动管理缓存
    public void deleteBySessionId(final Session Session) {
        ((SessionMapper) mapper).deleteBySessionId(Session.getSessionId());
    }

    public List<String> querySessionIdByAccount(Session Session) {
        return ((SessionMapper) mapper).querySessionIdByAccount(Session.getAccount());
    }

    //
    public void cleanExpiredSessions() {
        String key = "spring:session:" + PropertiesUtil.getString("session.redis.namespace.shuo") + ":sessions:expires:";
        Map<String, Object> columnMap = InstanceUtil.newHashMap();
        List<Session> sessions = queryList(columnMap);
        for (Session Session : sessions) {
            logger.info("检查SESSION : {}", Session.getSessionId());
            if (!CacheUtil.getCache().exists(key + Session.getSessionId())) {
                logger.info("移除SESSION : {}", Session.getSessionId());
                delete(Session.getId());
            }
        }
    }
}