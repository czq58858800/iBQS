package com.bq.shuo.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.shuo.model.UserWeibo;
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
public interface UserWeiboMapper extends BaseMapper<UserWeibo> {

    String queryByOpenId(@Param("openId") String openId, @Param("userId") String userId);

    List<String> selectIdPageByFollow(Page<String> idPage, @Param("cm")Map<String, Object> params);
}