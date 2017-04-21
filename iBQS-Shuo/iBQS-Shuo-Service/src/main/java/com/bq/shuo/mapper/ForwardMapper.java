package com.bq.shuo.mapper;

import com.bq.shuo.model.Forward;
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
public interface ForwardMapper extends BaseMapper<Forward> {

    int selectCountBySubjectId(@Param("subjectId") String subjectId);

    Integer selectCountByUserId(@Param("userId") String userId);

}