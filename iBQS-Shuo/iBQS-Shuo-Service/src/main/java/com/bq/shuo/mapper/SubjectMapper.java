package com.bq.shuo.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.shuo.model.Subject;
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
public interface SubjectMapper extends BaseMapper<Subject> {

    int selectSubjectCounter(@Param("subjectId")String subjectId, @Param("field")String field);

    int selectCountByUserId(@Param("userId")String userId);

    List<String> selectRandIdByMap(@Param("cm")Map<String, Object> params);

    List<String> queryByNew(@Param("cm")Map<String, Object> params);

    int selectRowByMap(@Param("cm") Map<String, Object> params);

    List<String> selectByKeyword(Page<String> idPage, @Param("cm")Map<String, Object> params);
    List<String> selectByKeyword(@Param("cm")Map<String, Object> params);

    String selectHashById(String hashCode);

    void updateCounter(@Param("subjectId") String subjectId,@Param("field") String field, @Param("number")Integer number);

    int selectIsReleaseSubject(@Param("topic") String topic, @Param("userId") String userId);
}