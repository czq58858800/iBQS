package com.bq.web.shuo;

import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.model.Topics;
import com.bq.shuo.provider.IShuoProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

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
        return super.query(modelMap, param);
    }

    @ApiOperation(value = "话题详情")
    @RequiresPermissions("shuo.topic.read")
    @PutMapping(value = "/read/detail")
    public Object get(ModelMap modelMap, @RequestBody Topics param) {
        return super.get(modelMap, param);
    }

    @PostMapping
    @ApiOperation(value = "修改话题")
    @RequiresPermissions("shuo.topic.update")
    public Object update(ModelMap modelMap, @RequestBody Topics param) {
        return super.update(modelMap, param);
    }

    @DeleteMapping
    @ApiOperation(value = "删除话题")
    @RequiresPermissions("shuo.topic.delete")
    public Object delete(ModelMap modelMap, @RequestBody Topics param) {
        return super.delete(modelMap, param);
    }
}