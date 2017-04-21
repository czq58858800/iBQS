package com.bq.shuo.mapper;

import com.bq.shuo.core.base.BaseMapper;
import com.bq.shuo.model.UserConfig;

/**
 * Created by Administrator on 2017/4/14 0014.
 */
public interface UserConfigMapper extends BaseMapper<UserConfig> {
    String selectByUserId(String userId);
}
