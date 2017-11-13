package com.bq.shuo.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.shuo.model.Category;
import com.bq.shuo.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author chern.zq
 * @since 2017-04-13
 */
public interface CategoryMapper extends BaseMapper<Category> {

    int selectCategoryCounter(@Param("categoryId") String categoryId, @Param("field")String field);

    List<String> selectIdByUserMap(Page<String> page, @Param("cm")Map<String, Object> params);
    int selectCountByUserMap(@Param("cm")Map<String, Object> params);
}