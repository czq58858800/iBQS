package com.bq.shuo.mapper;

import com.bq.shuo.model.CategoryCollection;
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
public interface CategoryCollectionMapper extends BaseMapper<CategoryCollection> {

    String selectCollById(@Param("categoryId") String categoryId, @Param("userId") String userId);

    int selectCountByUserId(@Param("userId") String userId);

    int selectMyCollCountByUserId(@Param("userId") String userId);

    int selectCountByCategoryId(@Param("categoryId") String categoryId);
}