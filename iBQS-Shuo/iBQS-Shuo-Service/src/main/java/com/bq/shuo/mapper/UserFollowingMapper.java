package com.bq.shuo.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.shuo.model.UserFollowing;
import com.bq.shuo.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
public interface UserFollowingMapper extends BaseMapper<UserFollowing> {

    int selectByFollowUserId(@Param("befollowUserId")String befollowUserId, @Param("followUserId")String followUserId);

    int selectFollowCountByUserId(@Param("userId") String userId);

    int selectFansCountByUserId(@Param("userId") String userId);

    List<String> selectUserIdByMap(Page<String> idPage, @Param("cm")Map<String, Object> params);

    String selectByFollowId(@Param("userId")String userId, @Param("beUserId")String beUserId);

    List<String> selectByUserIdFollows(String userId);
}