package com.bq.shuo.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.Constants;
import com.bq.core.config.Resources;
import com.bq.shuo.model.Comments;
import com.bq.shuo.model.Report;
import com.bq.shuo.core.base.BaseService;
import com.bq.shuo.model.Subject;
import com.bq.shuo.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *   服务实现类
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@Service
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"report")
public class ReportService extends BaseService<Report> {

    @Autowired
    private SubjectService subjectService;
    @Autowired
    private CommentsService commentsService;
    @Autowired
    private UserService userService;

    public Page<Report> queryBeans(Map<String, Object> params) {
        Page<Report> page = query(params);
        for (Report record:page.getRecords()) {
            String type = record.getType();//举报:(1:表情;2:评论;3:用户;)
            String valId = record.getValId();
            if (StringUtils.equals(type,"1")) {
                Subject subject = subjectService.queryById(valId);
                record.setValName(subject == null ? Resources.getMessage("ID_INCORRECT"):subject.getContent());
            } else if (StringUtils.equals(type,"2")) {
                Comments comments = commentsService.queryById(valId);
                record.setValName(comments == null ? Resources.getMessage("ID_INCORRECT"):comments.getContent());
            } else if (StringUtils.equals(type,"3")) {
                User user = userService.queryById(valId);
                record.setValName(user == null ? Resources.getMessage("ID_INCORRECT"):user.getName());
            }
            if (StringUtils.isNotBlank(record.getUserId())) {
                User user = userService.queryById(record.getUserId());
                record.setUserName(user.getName());
            }
        }
        return page;
    }
	
}