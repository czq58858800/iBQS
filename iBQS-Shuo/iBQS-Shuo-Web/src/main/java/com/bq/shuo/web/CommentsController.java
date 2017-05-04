package com.bq.shuo.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.support.Assert;
import com.bq.core.support.HttpCode;
import com.bq.core.util.WebUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.core.helper.PushType;
import com.bq.shuo.core.util.Push;
import com.bq.shuo.support.CommentsHelper;
import com.bq.shuo.model.Comments;
import com.bq.shuo.model.Subject;
import com.bq.shuo.provider.IShuoProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * CommentsController
 *
 * @author Harvey.Wei
 * @date 2016/11/14 0014
 */
@RestController
@Api(value = "评论接口", description = "评论接口")
@RequestMapping(value = "comments")
public class CommentsController extends AbstractController<IShuoProvider> {

    @Override
    public String getService() {
        return "commentsService";
    }

    // 评论列表
    @ApiOperation(value = "评论列表")
    @GetMapping("/list")
    public Object list(HttpServletRequest request, ModelMap modelMap,
                       @ApiParam(required = true, value = "页码") @RequestParam(value = "pageNum") Integer pageNum,
                       @ApiParam(required = true, value = "主题ID") @RequestParam(value = "subjectId") String subjectId) {
        Assert.notNull(pageNum, "PAGE_NUM");
        Assert.notNull(subjectId, "SUBJECT_ID");
        Map<String, Object> params = WebUtil.getParameterMap(request);
        params.put("currUserId",getCurrUser());
        params.put("enable",true);
        Parameter queryBeansParam = new Parameter(getService(),"queryBeans").setMap(params);
        Page page = provider.execute(queryBeansParam).getPage();
        page.setRecords(CommentsHelper.formatResultList(page.getRecords()));
        return setSuccessModelMap(modelMap,page);
    }

    /**
     * 评论主题
     * @param request
     * @param modelMap
     * @return
     */
    @ApiOperation(value = "评论主题")
    @PostMapping(value = "/add")
    public Object add(HttpServletRequest request, ModelMap modelMap,
              @ApiParam(required = true, value = "主题ID") @RequestParam(value = "subjectId") String subjectId,
              @ApiParam(required = true, value = "内容") @RequestParam(value = "content") String content,
              @ApiParam(required = false, value = "被回复者") @RequestParam(value = "beReplyId",required = false) String beReplyId,
              @ApiParam(required = false, value = "被回复评论ID") @RequestParam(value = "beReplyCommentId",required = false) String beReplyCommentId) {

        Assert.notNull(subjectId, "SUBJECT_ID");
        Assert.notNull(content, "CONTENT");

        if (StringUtils.isNotBlank(beReplyId) && StringUtils.isBlank(beReplyCommentId)) {
            Assert.notNull(beReplyCommentId, "BE_REPLY_COMMENT_ID");
        }

        if (StringUtils.isNotBlank(beReplyCommentId) && StringUtils.isBlank(beReplyId)) {
            Assert.notNull(beReplyId, "BE_REPLY_COMMENT_ID");
        }

        Comments record = new Comments(subjectId,content,beReplyId,beReplyCommentId);

        record.setUserId(getCurrUser());

        Subject subject = (Subject) provider.execute(new Parameter("subjectService","queryById").setId(subjectId)).getModel();

        Parameter parameter = new Parameter("blacklistService","selectIsBlacklistByUserId").setObjects(new Object[] {subject.getUserId(),getCurrUser()});
        boolean isBlacklist = (boolean) provider.execute(parameter).getObject();
        if (isBlacklist) {
            return setModelMap(modelMap, HttpCode.ALREADY_IN_BLACKLIST);
        }

        record = (Comments) provider.execute(new Parameter(getService(),"update").setModel(record)).getModel();

//        userService.sendPushQueue(new PushQueue(getCurrUser(),subject.getUserId(),subjectId,content,"1"));

        // 评论推送
        new Push(PushType.COMMENTS,getCurrUser(),subject.getUserId(),subjectId,record.getId(),content);

        return setSuccessModelMap(modelMap);
    }

    /**
     * 删除评论
     * @param request
     * @param modelMap
     * @return
     */
    @ApiOperation(value = "删除评论")
    @PostMapping(value = "/delete")
    public Object delete(HttpServletRequest request, ModelMap modelMap,
                         @ApiParam(required = true, value = "评论ID") @RequestParam(value = "id") String id) {
        Assert.notNull(id, "ID");
        Comments comments = (Comments) provider.execute(new Parameter(getService(),"queryById").setId(id)).getModel();

        if (!StringUtils.equals(comments.getUserId(),getCurrUser())) {
            // 当前登录用户ID与创建主题的用户ID不一致
            return setModelMap(modelMap,HttpCode.UNAUTHORIZED);
        }
        provider.execute(new Parameter(getService(),"delete").setObjects(new Object[] {id,getCurrUser()}));
        return setSuccessModelMap(modelMap);
    }

    /**
     * 评论点赞
     * @param request
     * @param modelMap
     * @return
     */
    @ApiOperation(value = "评论点赞")
    @PostMapping(value = "/praise")
    public Object praise(HttpServletRequest request, ModelMap modelMap,
                         @ApiParam(required = true, value = "评论ID") @RequestParam(value = "id") String id) {
        Assert.notNull(id, "ID");
        provider.execute(new Parameter(getService(),"insertCommentPraise").setObjects(new Object[] {id,getCurrUser()}));
        return setSuccessModelMap(modelMap);
    }

}
