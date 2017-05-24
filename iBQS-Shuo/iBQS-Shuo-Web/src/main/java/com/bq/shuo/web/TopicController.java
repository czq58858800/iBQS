package com.bq.shuo.web;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.support.HttpCode;
import com.bq.core.util.InstanceUtil;
import com.bq.core.util.Request2ModelUtil;
import com.bq.core.util.WebUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.core.util.QiniuUtil;
import com.bq.shuo.model.*;
import com.bq.shuo.provider.IShuoProvider;
import com.bq.shuo.support.SearchHelper;
import com.bq.shuo.support.SubjectHelper;
import com.bq.shuo.support.TopicHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * TopicController
 *
 * @author Harvey.Wei
 * @date 2016/10/11 0011
 */
@RestController
@Api(value = "话题接口", description = "话题接口")
@RequestMapping(value = "topic")
public class TopicController extends AbstractController<IShuoProvider> {

    @Override
    public String getService() {
        return "topicsService";
    }


    // 话题列表
    @ApiOperation(value = "话题列表")
    @GetMapping("/list")
    public Object list(HttpServletRequest request, ModelMap modelMap,
                       @ApiParam(required = true, value = "页码") @RequestParam(value = "pageNum") int pageNum,
                       @ApiParam(required = true, value = "类型(NEW:最新;HOT:最热;RCMD:推荐)") @RequestParam(value = "orderType") String orderType) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        params.put("currUserId", getCurrUser());
        params.put("enable", true);
        params.put("audit", "2");
        Parameter queryBeansParam = new Parameter(getService(), "queryBeans").setMap(params);
        Page page = provider.execute(queryBeansParam).getPage();
        page.setRecords(TopicHelper.formatResultList(page.getRecords()));
        return setSuccessModelMap(modelMap, page);
    }

    // 根据话题搜索主题列表
    @ApiOperation(value = "根据话题搜索主题列表")
    @GetMapping(value = "subject/list")
    public Object list(HttpServletRequest request, ModelMap modelMap,
                       @ApiParam(required = true, value = "话题") @RequestParam(value = "keyword") String keyword,
                       @ApiParam(required = false, value = "类型(NEW:最新;HOT:最热)") @RequestParam(value = "orderType", required = false) String orderType,
                       @ApiParam(required = true, value = "分页") @RequestParam(value = "pageNum") Integer pageNum) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        params.put("enable", true);
        params.put("currUserId", getCurrUser());
        keyword = keyword.replace("#","");
        params.put("keyword", "#" + keyword + "#");

        List<Object> resultList = InstanceUtil.newArrayList();
        Map<String, Object> resultMap = InstanceUtil.newHashMap();
        if (orderType != null) {
            if (StringUtils.equals(orderType, "1") || StringUtils.equals(orderType.toUpperCase(), "HOT"))
                params.put("topicOrderHot", true);

            params.remove("orderType");
        }
        Parameter queryBeansParam = new Parameter("subjectService", "queryBeans").setMap(params);
        Page page = provider.execute(queryBeansParam).getPage();

        Parameter queryBeansByKeywordParam = new Parameter("topicsService", "queryBeansByKeyword").setObjects(new Object[]{keyword, getCurrUser()});
        Topics topic = (Topics) provider.execute(queryBeansByKeywordParam).getModel();
        resultMap.put("topic", TopicHelper.formatResultMap(topic));
        resultMap.put("subject", SubjectHelper.formatResultList(page.getRecords()));
        resultList.add(resultMap);
        page.setRecords(resultList);
        return setSuccessModelMap(modelMap, page);
    }

    // 话题列表
//    @ApiOperation(value = "创建话题")
//    @PostMapping("/created")
//    public Object created(HttpServletRequest request, ModelMap modelMap,
//                          @ApiParam(required = true, value = "话题名称") @RequestParam(value = "name") String name,
//                          @ApiParam(required = false, value = "封面") @RequestParam(value = "cover", required = false) String cover,
//                          @ApiParam(required = true, value = "描述") @RequestParam(value = "summary") String summary) {
//        Topics record = new Topics(name, cover, summary);
//        if (StringUtils.isNotBlank(cover)) {
//            JSONObject imageInfo = QiniuUtil.getImageInfo(cover);
//            if (imageInfo.containsKey("format")) {
//                record.setCoverType(imageInfo.getString("format"));
//                record.setCoverWidth(imageInfo.getInteger("width"));
//                record.setCoverWidth(imageInfo.getInteger("height"));
//            }
//        }
//        record.setOwnerId(getCurrUser());
//        record.setAudit("1");
//        record.setViewNum(0);
//        record.setIsHot(false);
//        provider.execute(new Parameter("topicsService", "update").setModel(record));
//        return setSuccessModelMap(modelMap);
//    }

    // 修改话题
    @ApiOperation(value = "修改话题")
    @PostMapping("/update")
    public Object update(HttpServletRequest request, ModelMap modelMap,
                         @ApiParam(required = true, value = "话题Id") @RequestParam(value = "id") String id,
                         @ApiParam(required = false, value = "封面") @RequestParam(value = "cover", required = false) String cover,
                         @ApiParam(required = true, value = "描述") @RequestParam(value = "summary") String summary) {
        Topics record = (Topics) provider.execute(new Parameter("topicsService", "queryById").setId(id)).getModel();
        if (!StringUtils.equals(record.getOwnerId(), getCurrUser())) {
            // 无法修改，不是该话题的作者。
            return setModelMap(modelMap, HttpCode.FORBIDDEN);
        }
        record.setSummary(summary);
        if (StringUtils.isNotBlank(cover)) {
            JSONObject imageInfo = QiniuUtil.getImageInfo(cover);
            if (imageInfo.containsKey("format")) {
                record.setCoverType(imageInfo.getString("format"));
                record.setCoverWidth(imageInfo.getInteger("width"));
                record.setCoverWidth(imageInfo.getInteger("height"));
            }
        }
        provider.execute(new Parameter("topicsService", "update").setModel(record));
        return setSuccessModelMap(modelMap);
    }


    // 搜索列表
    @ApiOperation(value = "话题综合推荐")
    @GetMapping(value = "keyword/recomment")
    public Object keywordRecomment(HttpServletRequest request, ModelMap modelMap) {
        Map<String, Object> params = InstanceUtil.newHashMap();
        params.put("pageSize", 5);
        params.put("enable", true);
        Map<String, Object> dataMap = InstanceUtil.newHashMap();

        Parameter queryParam = new Parameter("searchService", "query").setMap(params);
        Page searchHotPage = provider.execute(queryParam).getPage();

        dataMap.put("search", SearchHelper.formatSearchHotResultList(searchHotPage.getRecords()));

        Parameter queryBeansParam = new Parameter("searchService", "queryBeans").setMap(params);
        Page topicsBeanPage = provider.execute(queryBeansParam).getPage();
        dataMap.put("topic", TopicHelper.formatResultList(topicsBeanPage.getRecords()));
        return setSuccessModelMap(modelMap, dataMap);
    }

    @ApiOperation(value = "话题主持人申请")
    @GetMapping(value = "owner/apply")
    public Object ownerApply(HttpServletRequest request,ModelMap modelMap,
             @ApiParam(required = true, value = "话题Id") @RequestParam(value = "id") String id) {
        Parameter parameter = new Parameter("topicsService","queryById").setId(id);
        Topics topics = (Topics) provider.execute(parameter).getModel();

        parameter = new Parameter("subjectService","selectIsReleaseSubject").setObjects(new Object[] {topics.getName(),getCurrUser()});
        boolean isRelease = (boolean) provider.execute(parameter).getObject();
        if (!isRelease) {
            return setModelMap(modelMap,HttpCode.UNRELEASED_SUBJECT);
        }
        if (topics.getOwnerStatus() == -1){
            return setModelMap(modelMap,HttpCode.NOT_ALLOW_APPLY_TOPIC);
        }
        if (topics.getOwnerStatus() == 2){
            parameter = new Parameter("userService","queryById").setId(getCurrUser());
            User user = (User) provider.execute(parameter).getModel();
            if (!StringUtils.equals("2",user.getUserType()) || !StringUtils.equals("3",user.getUserType())) {
                return setModelMap(modelMap, HttpCode.NOT_ALLOW_APPLY_TOPIC);
            }
        }
        return setSuccessModelMap(modelMap);
    }

    @ApiOperation(value = "话题主持人申请")
    @PostMapping(value = "owner/apply")
    public Object ownerApply(HttpServletRequest request,ModelMap modelMap,
             @ApiParam(required = true, value = "话题Id") @RequestParam(value = "id") String id,
             @ApiParam(required = true, value = "申请原因") @RequestParam(value = "reason") String reason,
             @ApiParam(required = true, value = "描述") @RequestParam(value = "summary") String summary) {
        Parameter parameter = new Parameter("topicsService","queryById").setId(id);
        Topics topics = (Topics) provider.execute(parameter).getModel();

        parameter = new Parameter("subjectService","selectIsReleaseSubject").setObjects(new Object[] {topics.getName(),getCurrUser()});
        boolean isRelease = (boolean) provider.execute(parameter).getObject();
        if (!isRelease) {
            return setModelMap(modelMap,HttpCode.UNRELEASED_SUBJECT);
        }
        if (topics.getOwnerStatus() == -1){
            return setModelMap(modelMap,HttpCode.NOT_ALLOW_APPLY_TOPIC);
        }
        if (topics.getOwnerStatus() == 2){
            parameter = new Parameter("userService","queryById").setId(getCurrUser());
            User user = (User) provider.execute(parameter).getModel();
            if (!StringUtils.equals("2",user.getUserType()) || !StringUtils.equals("3",user.getUserType())) {
                return setModelMap(modelMap, HttpCode.NOT_ALLOW_APPLY_TOPIC);
            }
        }
        TopicsReview record = new TopicsReview(id,reason,getCurrUser(),summary);
        parameter = new Parameter("topicsReviewService","update").setModel(record);
        provider.execute(parameter);
        return setSuccessModelMap(modelMap);
    }


}