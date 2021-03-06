package com.bq.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import com.bq.model.TaskFireLog;

import com.baomidou.mybatisplus.mapper.BaseMapper;

public interface TaskFireLogMapper extends BaseMapper<TaskFireLog> {
    List<String> selectIdByMap(RowBounds rowBounds, @Param("cm") Map<String, Object> params);
}
