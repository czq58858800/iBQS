package com.bq.mapper;

import com.bq.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.bq.model.SysUserThirdparty;

public interface SysUserThirdpartyMapper extends BaseMapper<SysUserThirdparty> {
	String queryUserIdByThirdParty(@Param("provider") String provider, @Param("openId") String openId);
}