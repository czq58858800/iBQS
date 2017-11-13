package com.bq.shuo.mapper;

import com.bq.shuo.model.Comments;
import com.bq.shuo.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author chern.zq
 * @since 2017-04-13
 */
public interface CommentsMapper extends BaseMapper<Comments> {

    int selectCountBySubjectId(@Param("subjectId") String subjectId);

    int selectCommentIdByPraiseCount(@Param("commentId")String commentId);

    void deleteCounter(String id);
    void deleteByCurrUserId(@Param("currUserId")String currUserId, @Param("id")String id);

    void insertCommentPraise(@Param("commentId")String commentId, @Param("userId")String userId);

    void updateCounter(@Param("commentsId") String commentsId,@Param("field") String field, @Param("number")Integer number);
}