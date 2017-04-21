package com.bq.mapper;

import java.util.List;
import java.util.Map;

import com.bq.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.bq.model.SysDic;

public interface SysDicMapper extends BaseMapper<SysDic> {
    List<String> selectIdPage(@Param("cm") Map<String, Object> params);
}