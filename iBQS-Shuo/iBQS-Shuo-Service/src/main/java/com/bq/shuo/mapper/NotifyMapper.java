package com.bq.shuo.mapper;

import com.bq.shuo.model.Notify;
import com.bq.shuo.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
public interface NotifyMapper extends BaseMapper<Notify> {

    int getByUnreadNum(@Param("cm") Map<String, Object> params);

    void updateRead(@Param("userId")String userId,@Param("msgType")String msgType);

}