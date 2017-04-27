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
import com.bq.shuo.model.SearchHot;
import com.bq.shuo.model.Subject;
import com.bq.shuo.model.Topics;
import com.bq.shuo.model.TopicsReview;
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
        Map<String,Object> params = WebUtil.getParameterMap(request);
        params.put("currUserId",getCurrUser());
        params.put("enable",true);
        params.put("audit","2");
        Parameter queryBeansParam = new Parameter(getService(),"queryBeans").setMap(params);
        Page page = provider.execute(queryBeansParam).getPage();
        page.setRecords(TopicHelper.formatResultList(page.getRecords()));
        return setSuccessModelMap(modelMap,page);
    }

    // 根据话题搜索主题列表
    @ApiOperation(value = "根据话题搜索主题列表")
    @GetMapping(value = "subject/list")
    public Object list(HttpServletRequest request, ModelMap modelMap,
                       @ApiParam(required = true, value = "话题") @RequestParam(value = "keyword") String keyword,
                       @ApiParam(required = false, value = "类型(NEW:最新;HOT:最热)") @RequestParam(value = "orderType",required = false) String orderType,
                       @ApiParam(required = true, value = "分页") @RequestParam(value = "pageNum") Integer pageNum) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        params.put("currUserId",getCurrUser());
        List<Object> resultList = InstanceUtil.newArrayList();
        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        if (orderType != null) {
            if (StringUtils.equals(orderType,"1") || StringUtils.equals(orderType.toUpperCase(),"HOT"))
                params.put("topicOrderHot",true);

            params.remove("orderType");
        }
        Parameter queryBeansParam = new Parameter("subjectService","queryBeans").setMap(params);
        Page page = provider.execute(queryBeansParam).getPage();

        Parameter queryBeansByKeywordParam = new Parameter("topicsService","queryBeansByKeyword").setObjects(new Object[] {keyword,getCurrUser()});
        Topics topic = (Topics) provider.execute(queryBeansByKeywordParam).getModel();
        if (topic != null) {
            if (topic.getId() != null) {
                resultMap.put("topic", TopicHelper.formatResultMap(topic));
            }
        }
        resultMap.put("subject", SubjectHelper.formatResultList(page.getRecords()));
        resultList.add(resultMap);
        page.setRecords(resultList);
        return setSuccessModelMap(modelMap,page);
    }

    // 话题列表
    @ApiOperation(value = "创建话题")
    @PostMapping("/created")
    public Object created(HttpServletRequest request, ModelMap modelMap,
                          @ApiParam(required = true, value = "话题名称") @RequestParam(value = "name") String name,
                          @ApiParam(required = false, value = "封面") @RequestParam(value = "cover", required = false) String cover,
                          @ApiParam(required = true, value = "描述") @RequestParam(value = "summary") String summary) {
        Topics record = new Topics(name,cover,summary);
        if (StringUtils.isNotBlank(cover)) {
            JSONObject imageInfo = QiniuUtil.getImageInfo(cover);
            if (imageInfo.containsKey("format")) {
                record.setCoverType(imageInfo.getString("format"));
                record.setCoverWidth(imageInfo.getInteger("width"));
                record.setCoverWidth(imageInfo.getInteger("height"));
            }
        }
        record.setOwnerId(getCurrUser());
        record.setAudit("1");
        record.setViewNum(0);
        record.setIsHot(false);
        provider.execute(new Parameter("topicsService","update").setModel(record));
        TopicsReview topicsReview = new TopicsReview(record.getId(),name,getCurrUser(),cover,record.getCoverType(),record.getCoverWidth(),record.getCoverHeight(),summary);
        provider.execute(new Parameter("topicsReviewService","update").setModel(topicsReview));

        return setSuccessModelMap(modelMap);
    }

    // 修改话题
    @ApiOperation(value = "修改话题")
    @PostMapping("/update")
    public Object update(HttpServletRequest request, ModelMap modelMap,
                         @ApiParam(required = true, value = "话题Id") @RequestParam(value = "id") String id,
                         @ApiParam(required = false, value = "封面") @RequestParam(value = "cover", required = false) String cover,
                         @ApiParam(required = true, value = "描述") @RequestParam(value = "summary") String summary) {
        Topics record = (Topics) provider.execute(new Parameter("topicsService","queryById").setId(id)).getModel();
        if (!StringUtils.equals(record.getOwnerId(), getCurrUser())) {
            // 无法修改，不是该话题的作者。
            return setModelMap(modelMap, HttpCode.FORBIDDEN);
        }
        TopicsReview topicsReview = new TopicsReview(record.getId(),record.getName(),getCurrUser(),cover,record.getCoverType(),record.getCoverWidth(),record.getCoverHeight(),summary);
        if (StringUtils.isNotBlank(cover)) {
            JSONObject imageInfo = QiniuUtil.getImageInfo(cover);
            if (imageInfo.containsKey("format")) {
                topicsReview.setCoverType(imageInfo.getString("format"));
                topicsReview.setCoverWidth(imageInfo.getInteger("width"));
                topicsReview.setCoverWidth(imageInfo.getInteger("height"));
            }
        }
        provider.execute(new Parameter("topicsReviewService","update").setModel(topicsReview));
        return setSuccessModelMap(modelMap);
    }


    // 搜索列表
    @ApiOperation(value = "话题综合推荐")
    @GetMapping(value = "keyword/recomment")
    public Object keywordRecomment(HttpServletRequest request, ModelMap modelMap) {
        Map<String, Object> params = InstanceUtil.newHashMap();
        params.put("pageSize",5);
        Map<String,Object> dataMap = InstanceUtil.newHashMap();

        Parameter queryParam = new Parameter("searchService","query").setMap(params);
        Page searchHotPage = provider.execute(queryParam).getPage();

        dataMap.put("search", SearchHelper.formatSearchHotResultList(searchHotPage.getRecords()));

        Parameter queryBeansParam = new Parameter("searchService","queryBeans").setMap(params);
        Page topicsBeanPage = provider.execute(queryBeansParam).getPage();
        dataMap.put("topic",TopicHelper.formatResultList(topicsBeanPage.getRecords()));
        return  setSuccessModelMap(modelMap,dataMap);
    }
}
