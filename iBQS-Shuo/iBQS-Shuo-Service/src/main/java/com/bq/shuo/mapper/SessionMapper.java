package com.bq.shuo.mapper;

import com.bq.shuo.model.Session;
import com.bq.shuo.core.base.BaseMapper;

import java.util.List;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author chern.zq
 * @since 2017-04-13
 */
public interface SessionMapper extends BaseMapper<Session> {

    void deleteBySessionId(String sessionId);

    String queryBySessionId(String sessionId);

    List<String> querySessionIdByAccount(String account);

}