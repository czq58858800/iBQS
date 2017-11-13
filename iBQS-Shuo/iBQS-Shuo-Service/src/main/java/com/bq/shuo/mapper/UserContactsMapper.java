package com.bq.shuo.mapper;

import com.bq.shuo.model.UserContacts;
import com.bq.shuo.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

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
public interface UserContactsMapper extends BaseMapper<UserContacts> {
    List<String> selectIdPageByFollow(RowBounds rowBounds, @Param("cm") Map<String, Object> params);

    String selectByPhone(@Param("deviceId")String deviceId,@Param("userId") String userId, @Param("phone")String phone);
}