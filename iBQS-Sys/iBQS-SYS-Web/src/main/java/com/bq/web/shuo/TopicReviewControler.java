package com.bq.web.shuo;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.model.Subject;
import com.bq.shuo.model.Topics;
import com.bq.shuo.provider.IShuoProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * 话题管理
 *
 * @author Harvey.Wei
 * @version 2016年4月2日 下午4:20:10
 */
@RestController
@Api(value = "话题主持人管理", description = "话题主持人管理")
@RequestMapping(value = "/shuo/topic/review")
public class TopicReviewControler extends AbstractController<IShuoProvider> {
    @Override
    public String getService() {
        return "topicsReviewService";
    }


    @ApiOperation(value = "查询话题主持人")
    @RequiresPermissions("shuo.topic.review.read")
    @PutMapping(value = "/read/glist")
    public Object gquery(ModelMap modelMap, @RequestBody Map<String, Object> param) {
        Parameter parameter = new Parameter(getService(), "queryGroupTopic").setMap(param);
        logger.debug("{} execute queryGroupTopic start...", parameter.getNo());
        Page<?> list = provider.execute(parameter).getPage();
        logger.debug("{} execute queryGroupTopic end.", parameter.getNo());
        return setSuccessModelMap(modelMap, list);
    }

    @ApiOperation(value = "查询话题主持人")
    @RequiresPermissions("shuo.topic.review.read")
    @PutMapping(value = "/read/list")
    public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
        return super.query(modelMap, param);
    }

    @ApiOperation(value = "话题主持人详情")
    @RequiresPermissions("shuo.topic.review.read")
    @PutMapping(value = "/read/detail")
    public Object get(ModelMap modelMap, @RequestBody Topics param) {
        return super.get(modelMap, param);
    }


    @ApiOperation(value = "修改话题主持人")
    @RequiresPermissions("shuo.topic.review.update")
    @PostMapping("/update")
    public Object update(ModelMap modelMap, @RequestBody Topics param) {
        return super.update(modelMap, param);
    }

    @DeleteMapping
    @ApiOperation(value = "删除话题主持人")
    @RequiresPermissions("shuo.topic.review.delete")
    public Object delete(ModelMap modelMap, @RequestBody Topics param) {
        return super.delete(modelMap, param);
    }
}
