package com.bq.mapper;

import java.util.List;
import java.util.Map;

import com.bq.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.bq.model.SysUser;

public interface SysUserMapper extends BaseMapper<SysUser> {
    List<String> selectIdPage(@Param("cm") Map<String, Object> params);
}
