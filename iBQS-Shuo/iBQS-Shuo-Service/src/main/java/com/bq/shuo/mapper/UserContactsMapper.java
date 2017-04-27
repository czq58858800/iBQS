package com.bq.shuo.mapper;

import com.bq.shuo.model.UserContacts;
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
public interface UserContactsMapper extends BaseMapper<UserContacts> {


    String selectByPhone(@Param("userId") String userId, @Param("phone")String phone);
}