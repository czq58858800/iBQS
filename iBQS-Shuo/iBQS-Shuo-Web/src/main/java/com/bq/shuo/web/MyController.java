package com.bq.shuo.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.support.Assert;
import com.bq.core.support.HttpCode;
import com.bq.core.util.WebUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.provider.IShuoProvider;
import com.bq.shuo.support.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * MyController
 *
 * @author Harvey.Wei
 * @date 2016/11/23 0023
 */
@RestController
@Api(value = "我的接口", description = "我的接口")
@RequestMapping(value = "my")
public class MyController extends AbstractController<IShuoProvider> {
    public String getService() {
        return "";
    }

    // 我的作品列表
    @ApiOperation(value = "我的作品列表")
    @GetMapping("/works")
    public Object works(HttpServletRequest request, ModelMap modelMap,
                @ApiParam(required = true, value = "页码") @RequestParam(value = "pageNum") Integer pageNum,
                @ApiParam(required = false, value = "用户ID") @RequestParam(value = "userId",required = false) String userId) {
        Assert.notNull(pageNum, "PAGE_NUM");

        Map<String, Object> params = WebUtil.getParameterMap(request);
        if (StringUtils.isBlank(userId)) {
            if (StringUtils.isBlank(getCurrUser())) return setModelMap(modelMap, HttpCode.UNAUTHORIZED);
            params.put("userId",getCurrUser());
        }
        params.put("enable",true);
        params.put("currUserId",getCurrUser());
        params.put("myWorks",true);
        Parameter queryBeansParam = new Parameter("subjectService","queryMoreBeans").setMap(params);
        Page page = provider.execute(queryBeansParam).getPage();
        page.setRecords(SubjectHelper.formatResultList(page.getRecords()));
        return setSuccessModelMap(modelMap, page);
    }

    // 我的转发列表
    @ApiOperation(value = "我的转发列表")
    @GetMapping("/forwards")
    public Object forwards(HttpServletRequest request, ModelMap modelMap,
                       @ApiParam(required = true, value = "页码") @RequestParam(value = "pageNum") Integer pageNum,
                       @ApiParam(required = false, value = "用户ID") @RequestParam(value = "userId",required = false) String userId) {
        Assert.notNull(pageNum, "PAGE_NUM");

        Map<String, Object> params = WebUtil.getParameterMap(request);
        if (StringUtils.isBlank(userId)) {
            if (StringUtils.isBlank(getCurrUser())) return setModelMap(modelMap, HttpCode.UNAUTHORIZED);
            params.put("userId",getCurrUser());
        }
        params.put("currUserId",getCurrUser());
        Parameter queryBeansParam = new Parameter("forwardService","queryBeans").setMap(params);
        Page page = provider.execute(queryBeansParam).getPage();
        page.setRecords(ForwardHelper.formatResultList(page.getRecords()));
        return setSuccessModelMap(modelMap, page);
    }

    // 我的关注列表
    @ApiOperation(value = "我的关注列表")
    @GetMapping("/follow")
    public Object follow(HttpServletRequest request, ModelMap modelMap,
             @ApiParam(required = true, value = "页码") @RequestParam(value = "pageNum") Integer pageNum,
             @ApiParam(required = false, value = "页数") @RequestParam(value = "pageSize",required = false) Integer pageSize,
             @ApiParam(required = false, value = "用户ID") @RequestParam(value = "userId",required = false) String userId) {
        Assert.notNull(pageNum, "PAGE_NUM");
        Map<String, Object> params = WebUtil.getParameterMap(request);
        if (StringUtils.isBlank(userId)) {
            if (StringUtils.isBlank(getCurrUser())) return setModelMap(modelMap, HttpCode.UNAUTHORIZED);
            params.put("followUserId",getCurrUser());
        } else {
            params.remove("userId");
            params.put("followUserId",userId);
        }
        params.put("currUserId",getCurrUser());
        Parameter queryBeansParam = new Parameter("userFollowingService","queryByUserBeans").setMap(params);
        Page page = provider.execute(queryBeansParam).getPage();
        page.setRecords(UserHelper.formatResultList(page.getRecords()));
        return setSuccessModelMap(modelMap, page);
    }

    // 我的粉丝列表
    @ApiOperation(value = "我的粉丝列表")
    @GetMapping("/fans")
    public Object fans(HttpServletRequest request, ModelMap modelMap,
               @ApiParam(required = true, value = "页码") @RequestParam(value = "pageNum") Integer pageNum,
               @ApiParam(required = false, value = "用户ID") @RequestParam(value = "userId",required = false) String userId) {
        Assert.notNull(pageNum, "PAGE_NUM");
        Map<String, Object> params = WebUtil.getParameterMap(request);
        if (StringUtils.isBlank(userId)) {
            if (StringUtils.isBlank(getCurrUser())) return setModelMap(modelMap, HttpCode.UNAUTHORIZED);
            params.put("befollowUserId",getCurrUser());
        } else {
            params.remove("userId");
            params.put("befollowUserId",userId);
        }
        params.put("currUserId",getCurrUser());
        Parameter queryBeansParam = new Parameter("userFollowingService","queryByUserBeans").setMap(params);
        Page page = provider.execute(queryBeansParam).getPage();
        page.setRecords(UserHelper.formatFollowResultList(page.getRecords()));
        return setSuccessModelMap(modelMap, page);
    }

    @ApiOperation(value = "我的素材分类列表")
    @GetMapping("/material/category/list")
    public Object categoryList(HttpServletRequest request, ModelMap modelMap,
                               @ApiParam(required = true, value = "页码") @RequestParam(value = "pageNum") Integer pageNum,
                               @ApiParam(required = false, value = "用户ID") @RequestParam(value = "userId",required = false) String userId) {
        Assert.notNull(pageNum, "PAGE_NUM");

        Map<String, Object> params = WebUtil.getParameterMap(request);
        params.put("enable",true);

        if (StringUtils.isBlank(userId)) {
            if (StringUtils.isBlank(getCurrUser())) return setModelMap(modelMap, HttpCode.UNAUTHORIZED);
            params.put("userId",getCurrUser());
        } else {
            params.put("stuffNumLT",true);
        }


        Parameter queryBeansParam = new Parameter("categoryService","query").setMap(params);
        Page page = provider.execute(queryBeansParam).getPage();
        page.setRecords(CategoryHelper.formatCategoryResultList(page.getRecords()));
        return setSuccessModelMap(modelMap, page);
    }

    @ApiOperation(value = "我喜欢的主题列表")
    @GetMapping("/liked/subject/list")
    public Object likedSubjectList(HttpServletRequest request, ModelMap modelMap,
                       @ApiParam(required = true, value = "页码") @RequestParam(value = "pageNum") Integer pageNum,
                       @ApiParam(required = false, value = "用户ID") @RequestParam(value = "userId",required = false) String userId) {
        Assert.notNull(pageNum, "PAGE_NUM");
        Map<String, Object> params = WebUtil.getParameterMap(request);
        if (StringUtils.isBlank(userId)) {
            if (StringUtils.isBlank(getCurrUser())) return setModelMap(modelMap, HttpCode.UNAUTHORIZED);
            params.put("userId",getCurrUser());
        }
        params.put("currUserId",getCurrUser());
        Parameter queryBeansParam = new Parameter("subjectLikedService","queryBeans").setMap(params);
        Page page = provider.execute(queryBeansParam).getPage();
        page.setRecords(SubjectHelper.formatSubjectLikedResultList(page.getRecords()));
        return setSuccessModelMap(modelMap, page);
    }

    @ApiOperation(value = "我喜欢的专辑列表")
    @GetMapping(value = "/liked/album/list")
    public Object albumLikedList(HttpServletRequest request,ModelMap modelMap,
            @ApiParam(required = true, value = "页码") @RequestParam(value = "pageNum") Integer pageNum,
            @ApiParam(required = false, value = "用户ID") @RequestParam(value = "userId",required = false) String userId) {
        Assert.notNull(pageNum, "PAGE_NUM");
        Map<String, Object> params = WebUtil.getParameterMap(request);
        if (StringUtils.isBlank(userId)) {
            if (StringUtils.isBlank(getCurrUser())) return setModelMap(modelMap, HttpCode.UNAUTHORIZED);
            params.put("userId",getCurrUser());
        }
        params.put("currUserId",getCurrUser());
        Parameter queryBeansParam = new Parameter("albumLikedService","queryBeans").setMap(params);
        Page page = provider.execute(queryBeansParam).getPage();
        page.setRecords(AlbumHelper.formatLikedResultList(page.getRecords()));
        return setSuccessModelMap(modelMap, page);
    }

    @ApiOperation(value = "我的话题列表")
    @GetMapping("/topics/list")
    public Object topicsList(HttpServletRequest request, ModelMap modelMap,
                             @ApiParam(value = "审核状态(-1：审核失败；1：审核中；2：审核通过）") @RequestParam(value = "audit",required = false) Integer audit,
                             @ApiParam(required = true, value = "页码") @RequestParam(value = "pageNum") Integer pageNum) {
        Assert.notNull(pageNum, "PAGE_NUM");
        Map<String, Object> params = WebUtil.getParameterMap(request);
        if (StringUtils.isBlank(getCurrUser())) return setModelMap(modelMap, HttpCode.UNAUTHORIZED);
        params.put("ownerId", getCurrUser());
        Parameter queryBeansParam = new Parameter("topicsService","query").setMap(params);
        Page page = provider.execute(queryBeansParam).getPage();
        page.setRecords(TopicHelper.formatResultList(page.getRecords()));
        return setSuccessModelMap(modelMap, page);
    }

    @ApiOperation(value = "我收藏的素材分类列表")
    @GetMapping("/coll/cateogry/list")
    public Object collCategoryList(HttpServletRequest request, ModelMap modelMap,
                                   @ApiParam(required = true, value = "页码") @RequestParam(value = "pageNum") Integer pageNum) {
        Assert.notNull(pageNum, "PAGE_NUM");
        Map<String, Object> params = WebUtil.getParameterMap(request);
        params.put("userId",getCurrUser());
        params.put("currUserId",getCurrUser());
        Parameter queryBeansParam = new Parameter("categoryCollectionService","queryBeans").setMap(params);
        Page page = provider.execute(queryBeansParam).getPage();
        page.setRecords(CategoryHelper.formatCategoryResultList(page.getRecords()));
        return setSuccessModelMap(modelMap, page);
    }
}
