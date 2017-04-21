package com.bq.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.bq.core.base.BaseMapper;
import com.bq.model.SysUserMenu;

public interface SysUserMenuMapper extends BaseMapper<SysUserMenu> {
	List<String> queryMenuIdsByUserId(@Param("userId") String userId);

	List<Map<String, Object>> queryPermissions(@Param("userId") String userId);

	List<String> queryPermission(@Param("userId") String id);
}