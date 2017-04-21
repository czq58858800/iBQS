package com.bq.mapper;

import java.util.List;
import java.util.Map;

import com.bq.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.bq.model.SysRoleMenu;

public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
	List<String> queryMenuIdsByRoleId(@Param("roleId") String roleId);

	List<Map<String, Object>> queryPermissions(@Param("roleId") String roleId);

	List<String> queryPermission(@Param("roleId") String id);
}
