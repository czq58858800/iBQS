package com.bq.shuo.mapper;

import com.bq.shuo.model.UserThirdparty;
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
public interface UserThirdpartyMapper extends BaseMapper<UserThirdparty> {
    String queryIdByThireParty(@Param("provider")String provider, @Param("openId")String openId);
    List<String> selectIdByList(String userId);
    String queryThirdPartyByUserId(@Param("userId")String userId, @Param("provider")String thirdProvider);
    int selectCountByUserId(String userId);
}