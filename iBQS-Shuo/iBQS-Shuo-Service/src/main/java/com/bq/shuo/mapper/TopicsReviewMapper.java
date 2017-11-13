package com.bq.shuo.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.shuo.model.TopicsReview;
import com.bq.shuo.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

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
public interface TopicsReviewMapper extends BaseMapper<TopicsReview> {


    List<String> selectIdByGroupTopic(Page<String> page, @Param("cm") Map<String, Object> params);
}