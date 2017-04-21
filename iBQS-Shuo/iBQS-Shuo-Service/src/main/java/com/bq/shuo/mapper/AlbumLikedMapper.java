package com.bq.shuo.mapper;

import com.bq.shuo.model.AlbumLiked;
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
public interface AlbumLikedMapper extends BaseMapper<AlbumLiked> {

    String selectLikedById(@Param("albumId") String albumId, @Param("userId") String userId);

    int selectCountByUserId(@Param("userId") String userId);
}