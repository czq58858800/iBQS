package com.bq.web.shuo;

import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.BaseModel;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.model.Subject;
import com.bq.shuo.model.Tag;
import com.bq.shuo.model.Topics;
import com.bq.shuo.provider.IShuoProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 话题管理
 *
 * @author Harvey.Wei
 * @version 2016年4月2日 下午4:20:10
 */
@RestController
@Api(value = "话题管理", description = "话题管理")
@RequestMapping(value = "/shuo/topic")
public class TopicControler extends AbstractController<IShuoProvider> {
    @Override
    public String getService() {
        return "topicsService";
    }

    @ApiOperation(value = "查询话题")
    @RequiresPermissions("shuo.topic.read")
    @PutMapping(value = "/read/list")
    public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
        Parameter parameter = new Parameter("tagService","queryByAll");
        List<Tag> tagList = (List<Tag>) provider.execute(parameter).getList();
        modelMap.put("tags",tagList);
        return super.query(modelMap, param);
    }

    @ApiOperation(value = "话题详情")
    @RequiresPermissions("shuo.topic.read")
    @PutMapping(value = "/read/detail")
    public Object get(ModelMap modelMap, @RequestBody Topics param) {
        Parameter parameter = new Parameter(getService(), "queryById").setId(param.getId());
        BaseModel result = provider.execute(parameter).getModel();

        parameter = new Parameter("tagService","queryByAll");
        List<Tag> tagList = (List<Tag>) provider.execute(parameter).getList();
        modelMap.put("tags",tagList);
        return setSuccessModelMap(modelMap, result);
    }


    @ApiOperation(value = "修改话题")
    @RequiresPermissions("shuo.topic.update")
    @PostMapping("/update")
    public Object update(ModelMap modelMap, @RequestBody Topics param) {
        if (param.getTags() == null) {
            param.setTags(" ");
        }
        return super.update(modelMap, param);
    }

    @DeleteMapping
    @ApiOperation(value = "删除话题")
    @RequiresPermissions("shuo.topic.delete")
    public Object delete(ModelMap modelMap, @RequestBody Topics param) {
        return super.delete(modelMap, param);
    }

    @ApiOperation(value = "推荐热门话题")
    @RequiresPermissions("shuo.topic.update")
    @PostMapping(value = "/updateHot")
    public Object updateHot(ModelMap modelMap, @RequestBody Subject param) {
        Parameter parameter = new Parameter(getService(), "queryById").setId(param.getId());
        logger.debug("{} execute queryById start...", parameter.getNo());
        Topics record = (Topics) provider.execute(parameter).getModel();
        logger.debug("{} execute queryById end.", parameter.getNo());
        record.setIsHot(param.getIsHot());
        if (param.getIsHot()) {
            record.setHotTime(new Date());
        } else {
            record.setHotTime(null);
        }

        parameter = new Parameter(getService(), "update").setModel(param);
        logger.debug("{} execute update start...", parameter.getNo());
        provider.execute(parameter);
        logger.debug("{} execute update end.", parameter.getNo());
        return setSuccessModelMap(modelMap);
    }

}
