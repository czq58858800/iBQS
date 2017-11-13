/**
 * 
 */
package com.bq.shuo.core.base;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author chern.zq
 * 
 * @version 2016年6月3日 下午2:30:14
 * 
 */
public interface BaseMapper<T extends BaseModel> extends com.baomidou.mybatisplus.mapper.BaseMapper<T> {

	List<String> selectIdPage(@Param("cm") Map<String, Object> params);

	List<String> selectIdPage(RowBounds rowBounds, @Param("cm") Map<String, Object> params);

}
