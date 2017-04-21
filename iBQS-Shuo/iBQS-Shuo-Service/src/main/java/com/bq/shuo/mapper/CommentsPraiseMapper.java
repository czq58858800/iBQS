package com.bq.shuo.mapper;

import com.bq.shuo.model.CommentsPraise;
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
public interface CommentsPraiseMapper extends BaseMapper<CommentsPraise> {

    String selectPraiseById(@Param("commentId") String commentId, @Param("userId") String userId);
}