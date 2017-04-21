package com.bq.mapper;

import java.util.List;

import com.bq.core.base.BaseMapper;
import com.bq.model.SysDept;
import org.apache.ibatis.annotations.Param;

public interface SysDeptMapper extends BaseMapper<SysDept> {

	List<String> selectIdPage(@Param("cm") SysDept sysDept);
}