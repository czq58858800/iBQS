package com.bq.shuo.mapper;

import com.bq.shuo.model.Blacklist;
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
public interface BlacklistMapper extends BaseMapper<Blacklist> {

    String selectIsBlacklistByUserId(@Param("userId") String userId, @Param("blacklistUserId") String blacklistUserId);

}