package com.bq.shuo.mapper;

import com.bq.shuo.model.Material;
import com.bq.shuo.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author chern.zq
 * @since 2017-04-13
 */
public interface MaterialMapper extends BaseMapper<Material> {

    int selectCountByCategory(@Param("categoryId") String categoryId);

    List<String> selectAll(@Param("categoryId") String categoryId);
}