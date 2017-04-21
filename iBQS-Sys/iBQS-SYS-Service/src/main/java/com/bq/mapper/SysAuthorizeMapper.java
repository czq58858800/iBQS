package com.bq.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.bq.model.SysMenu;

public interface SysAuthorizeMapper {

	void deleteUserMenu(@Param("userId") String userId, @Param("permission") String permission);

	void deleteUserRole(@Param("userId") String userId);

	void deleteRoleMenu(@Param("roleId") String roleId, @Param("permission") String permission);

	List<String> getAuthorize(@Param("userId") String userId);

	List<String> queryPermissionByUserId(@Param("userId") String userId);

	List<SysMenu> queryMenusPermission();
}
