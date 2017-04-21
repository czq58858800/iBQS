package com.bq.mapper;

import java.util.List;
import java.util.Map;

import com.bq.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.bq.model.SysMenu;

public interface SysMenuMapper extends BaseMapper<SysMenu> {
	/** 获取所有权限 */
	public List<Map<String, String>> getPermissions();

	public List<String> selectIdPage(@Param("cm") Map<String, Object> params);
}