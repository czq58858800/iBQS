package com.bq.shuo.mapper;

import com.bq.shuo.model.Album;
import com.bq.shuo.core.base.BaseMapper;

import java.util.List;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
public interface AlbumMapper extends BaseMapper<Album> {

    List<String> selectIdBySubjectId(String subjectId);

    void deleteBySubjectId(String id);
}