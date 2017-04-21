package com.bq.shuo.mapper;

import com.bq.shuo.model.MaterialHot;
import com.bq.shuo.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
public interface MaterialHotMapper extends BaseMapper<MaterialHot> {
    String selectByLibaryId(@Param("libraryId") String libraryId);
}