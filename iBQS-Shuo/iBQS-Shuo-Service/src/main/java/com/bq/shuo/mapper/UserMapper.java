package com.bq.shuo.mapper;

import com.bq.shuo.model.User;
import com.bq.shuo.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author chern.zq
 * @since 2017-04-13
 */
public interface UserMapper extends BaseMapper<User> {

    String selectOne(@Param("cm") Map<String, Object> params);

    void updateCounter(@Param("userId") String userId,@Param("field") String field, @Param("number")Integer number);
}