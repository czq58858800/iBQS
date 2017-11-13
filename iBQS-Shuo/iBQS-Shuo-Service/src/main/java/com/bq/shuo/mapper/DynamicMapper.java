package com.bq.shuo.mapper;

import com.bq.shuo.model.Dynamic;
import com.bq.shuo.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author chern.zq
 * @since 2017-04-13
 */
public interface DynamicMapper extends BaseMapper<Dynamic> {

    String selectValIdById(@Param("valId")String valId, @Param("type") String type);

}