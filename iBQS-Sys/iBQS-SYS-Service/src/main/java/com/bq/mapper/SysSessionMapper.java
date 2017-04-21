package com.bq.mapper;

import java.util.List;

import com.bq.core.base.BaseMapper;
import com.bq.model.SysSession;

public interface SysSessionMapper extends BaseMapper<SysSession> {

    void deleteBySessionId(String sessionId);

    String queryBySessionId(String sessionId);

    List<String> querySessionIdByAccount(String account);
}