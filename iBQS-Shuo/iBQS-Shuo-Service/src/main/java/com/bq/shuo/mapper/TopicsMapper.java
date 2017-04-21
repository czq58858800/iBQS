package com.bq.shuo.mapper;

import com.bq.shuo.model.Topics;
import com.bq.shuo.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
public interface TopicsMapper extends BaseMapper<Topics> {

    List<String> queryByHot(@Param("cm") Map<String, Object> params);

    List<String> queryByHot();
    Integer queryByHotCount(@Param("cm") Map<String, Object> params);

    String queryBeanByKeyword(@Param("keyword") String keyword);
}