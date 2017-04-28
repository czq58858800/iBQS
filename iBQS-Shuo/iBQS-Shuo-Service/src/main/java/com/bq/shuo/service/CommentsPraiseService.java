package com.bq.shuo.service;

import com.bq.core.Constants;
import com.bq.shuo.core.base.BaseService;
import com.bq.shuo.mapper.CommentsPraiseMapper;
import com.bq.shuo.model.CommentsPraise;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * <p>
 *   服务实现类
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@Service
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"commentsPraise")
public class CommentsPraiseService extends BaseService<CommentsPraise> {

    @Autowired
    private CommentsPraiseMapper commentsPraiseMapper;

    public String selectByPraiseId(String albumId, String userId) {
        return commentsPraiseMapper.selectPraiseById(albumId,userId);
    }

    public boolean selectByIsPraise(String commentsId, String userId) {
        String commentsPraiseId = selectByPraiseId(commentsId,userId);
        if (StringUtils.isNotBlank(commentsPraiseId)) {
            return true;
        }
        return false;
    }
	
}