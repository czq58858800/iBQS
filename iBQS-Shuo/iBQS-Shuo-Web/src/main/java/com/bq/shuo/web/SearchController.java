package com.bq.shuo.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.support.Assert;
import com.bq.core.support.HttpCode;
import com.bq.core.util.InstanceUtil;
import com.bq.core.util.WebUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.model.*;
import com.bq.shuo.provider.IShuoProvider;
import com.bq.shuo.support.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * SearchController
 *
 * @author Harvey.Wei
 * @date 2016/10/25 0025
 */
@RestController
@Api(value = "搜索接口", description = "搜索接口")
public class SearchController extends AbstractController<IShuoProvider> {
    public String getService() {
        return "searchHotService";
    }

    // 搜索-用户、主题、表情文字、标签
    @ApiOperation(value = "搜索-综合")
    @GetMapping(value = "search/synthetic")
    public Object searchSynthetic(HttpServletRequest request, ModelMap modelMap,
             @ApiParam(required = true, value = "关键字") @RequestParam(value = "keyword") String keyword,
             @ApiParam(required = true, value = "分页") @RequestParam(value = "pageNum") Integer pageNum) {
        Assert.notNull(pageNum, "PAGE_NUM");
        Assert.notNull(keyword, "KEYWORD");
        Map<String, Object> params = WebUtil.getParameterMap(request);
        params.put("currUserId",getCurrUser());
        params.put("myWorks",1);
        params.put("enable",true);


        Parameter queryBeansParam = new Parameter("subjectService","queryBeans").setMap(params);
        Page page = provider.execute(queryBeansParam).getPage();

        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        resultMap.put("total",page.getTotal());
        resultMap.put("pages",page.getPages());
        resultMap.put("current",page.getCurrent());
        resultMap.put("records", SubjectHelper.formatResultList(page.getRecords()));

        if (pageNum == 1) {
            User user = (User) provider.execute(new Parameter("userService","selectOne").setMap(params)).getModel();
            if (user!=null&&StringUtils.isNotBlank(user.getId())) {
                Map<String,Object> userMap = UserHelper.formatBriefResultMap(user);
                resultMap.put("user",userMap);
            }

            params.put("pageSize",4);
            Parameter queryTopicsBeansParam = new Parameter("topicsService","queryBeans").setMap(params);
            Page topicsPage = provider.execute(queryTopicsBeansParam).getPage();

            if (topicsPage.getRecords() != null && topicsPage.getRecords().size() > 0) {
                List<Map<String,Object>> topicResult = TopicHelper.formatResultList(topicsPage.getRecords());
                resultMap.put("topics",topicResult);
            }

        }

        return  setSuccessModelMap(modelMap,resultMap);
    }

    // 搜索-用户、主题、表情文字、标签
    @ApiOperation(value = "搜索-用户名称、帐号")
    @GetMapping(value = "search/user")
    public Object searchUser(HttpServletRequest request, ModelMap modelMap,
             @ApiParam(required = true, value = "关键字") @RequestParam(value = "keyword") String keyword,
             @ApiParam(required = true, value = "分页") @RequestParam(value = "pageNum") Integer pageNum) {
        Assert.notNull(pageNum, "PAGE_NUM");
        Assert.notNull(keyword, "KEYWORD");
        Map<String, Object> params = WebUtil.getParameterMap(request);

        params.put("currUserId",getCurrUser());
        params.put("orderbySearch",true);
        params.put("enable",true);
        Parameter queryBeansParam = new Parameter("userService","queryBeans").setMap(params);
        Page page = provider.execute(queryBeansParam).getPage();
//        appSearchService.update(keyword);
//        provider.execute(new Parameter("searchService","update").set(categoryReview));
        if (page.getRecords() != null && page.getRecords().size() > 0) {
            Map<String,Object> resultMap = UserHelper.formatResultPage(page);
            return setSuccessModelMap(modelMap,resultMap);
        }
        return  setModelMap(modelMap, HttpCode.NOT_DATA);
    }

    // 搜索-用户、主题、表情文字、标签
    @ApiOperation(value = "搜索-主题、表情文字、标签")
    @GetMapping(value = "search/subjects")
    public Object searchSubjects(HttpServletRequest request, ModelMap modelMap,
                         @ApiParam(required = true, value = "关键字") @RequestParam(value = "keyword") String keyword,
                         @ApiParam(required = true, value = "分页") @RequestParam(value = "pageNum") Integer pageNum) {
        Assert.notNull(pageNum, "PAGE_NUM");
        Assert.notNull(keyword, "KEYWORD");
        Map<String, Object> params = WebUtil.getParameterMap(request);
        params.put("myWorks",1);
        params.put("currUserId",getCurrUser());
        params.put("enable",true);
        Parameter parameter = new Parameter("subjectService","selectByKeyword").setMap(params);
        Page page = provider.execute(parameter).getPage();
        page.setRecords(SubjectHelper.formatResultList(page.getRecords()));
//        appSearchService.update(keyword);
        return  setSuccessModelMap(modelMap,page);
    }

    // 搜索-话题
    @ApiOperation(value = "搜索-话题")
    @GetMapping(value = "search/topic")
    public Object searchTopic(HttpServletRequest request, ModelMap modelMap,
                                 @ApiParam(required = true, value = "关键字") @RequestParam(value = "keyword") String keyword,
                                 @ApiParam(required = true, value = "分页") @RequestParam(value = "pageNum") Integer pageNum) {
        Assert.notNull(pageNum, "PAGE_NUM");
        Assert.notNull(keyword, "KEYWORD");
        Map<String, Object> params = WebUtil.getParameterMap(request);
        params.put("enable",true);
        Parameter queryBeansParam = new Parameter("topicsService","queryBeans").setMap(params);
        Page page = provider.execute(queryBeansParam).getPage();
        page.setRecords(TopicHelper.formatResultList(page.getRecords()));
//        appSearchService.update(keyword);
        return  setSuccessModelMap(modelMap,page);
    }

    // 搜索热词
    @ApiOperation(value = "搜索热词")
    @GetMapping(value = "search/hot")
    public Object searchHot(HttpServletRequest request, ModelMap modelMap,
                         @ApiParam(required = false, value = "页数") @RequestParam(value = "pageSize", required = false) String pageSize,
                         @ApiParam(required = true, value = "分页") @RequestParam(value = "pageNum") Integer pageNum) {
        Assert.notNull(pageNum, "PAGE_NUM");
        Map<String, Object> params = WebUtil.getParameterMap(request);
        params.put("enable",true);
        Parameter queryBeansParam = new Parameter("searchHotService","query").setMap(params);
        Page page = provider.execute(queryBeansParam).getPage();
        if (page.getRecords() != null && page.getRecords().size() > 0) {
            return setSuccessModelMap(modelMap, SearchHelper.formatSearchHotResultPage(page));
        }
        return  setSuccessModelMap(modelMap,page);
    }

    // 搜索列表
    @ApiOperation(value = "搜索列表")
    @GetMapping(value = "search/list")
    public Object list(HttpServletRequest request, ModelMap modelMap,
                            @ApiParam(required = true, value = "关键词") @RequestParam(value = "keyword") String keyword,
                            @ApiParam(required = false, value = "页数") @RequestParam(value = "pageSize", required = false) String pageSize,
                            @ApiParam(required = true, value = "分页") @RequestParam(value = "pageNum") Integer pageNum) {
        Assert.notNull(pageNum, "PAGE_NUM");
        Assert.notNull(keyword, "KEYWORD");
        Map<String, Object> params = WebUtil.getParameterMap(request);
        params.put("enable",true);
        Parameter queryParam = new Parameter("searchHotService","query").setMap(params);
        Page page = provider.execute(queryParam).getPage();
        if (page.getRecords() != null && page.getRecords().size() > 0) {
            return setSuccessModelMap(modelMap,SearchHelper.formatSearchHotResultPage(page));
        }
        return  setModelMap(modelMap, HttpCode.NOT_DATA);
    }

    // 搜索列表
    @ApiOperation(value = "贴纸综合")
    @GetMapping(value = "search/sticker")
    public Object sticker(HttpServletRequest request, ModelMap modelMap,
                       @ApiParam(required = true,value = "类型：syn:综合搜索；stk:贴纸；user:用户") @RequestParam(value = "t") String t,
                       @ApiParam(required = true,value = "关键词") @RequestParam(value = "keyword") String keyword,
                       @ApiParam(value = "页数") @RequestParam(value = "pageSize", required = false) String pageSize,
                       @ApiParam(required = true,value = "分页") @RequestParam(value = "pageNum") Integer pageNum) {
        Assert.notNull(t,"T");
        Assert.notNull(keyword,"KEYWORD");
        Assert.notNull(pageNum,"PAGE_NUM");

        Map<String, Object> params = WebUtil.getParameterMap(request);
        params.put("currUserId",getCurrUser());
        params.put("audit",2);
        params.put("orderType",2);
        params.put("enable",true);
        if (StringUtils.equals("SYN",t.trim().toUpperCase())) {
            Parameter queryCategoryBeansParam = new Parameter("categoryService","queryBeans").setMap(params);
            Page categoryPage = provider.execute(queryCategoryBeansParam).getPage();

            Parameter queryUserBeansParam = new Parameter("categoryService","queryUser").setMap(params);
            Page userPage = provider.execute(queryUserBeansParam).getPage();

            Map<String,Object> resultMap = InstanceUtil.newHashMap();
            resultMap.put("category",CategoryHelper.formatCategoryResultList(categoryPage.getRecords()));
            resultMap.put("user",CategoryHelper.formatCategoryResultList(userPage.getRecords()));
            return setSuccessModelMap(modelMap,resultMap);
        } else {
            if (StringUtils.equals("STK",t.trim().toUpperCase())) {
                // 贴纸
                Parameter queryCategoryBeansParam = new Parameter("categoryService","queryBeans").setMap(params);
                Page categoryPage = provider.execute(queryCategoryBeansParam).getPage();
                return setSuccessModelMap(modelMap, CategoryHelper.formatCategoryResultPage(categoryPage));
            }

            if (StringUtils.equals("USER",t.trim().toUpperCase())) {
                // 用户
                Parameter queryUserBeansParam = new Parameter("categoryService","queryUser").setMap(params);
                Page userPage = provider.execute(queryUserBeansParam).getPage();
                return setSuccessModelMap(modelMap,CategoryHelper.formatCategoryResultPage(userPage));
            }
        }

        return  setSuccessModelMap(modelMap);
    }
}
