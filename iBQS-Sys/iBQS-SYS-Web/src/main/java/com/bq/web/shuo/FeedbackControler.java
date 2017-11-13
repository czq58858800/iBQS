package com.bq.web.shuo;

import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.model.Feedback;
import com.bq.shuo.provider.IShuoProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 意见征集管理
 *
 * @author chern.zq
 * @version 2016年4月2日 下午4:20:10
 */
@RestController
@Api(value = "意见征集管理", description = "意见征集管理")
@RequestMapping(value = "/shuo/feedback")
public class FeedbackControler extends AbstractController<IShuoProvider> {
    @Override
    public String getService() {
        return "feedbackService";
    }

    @ApiOperation(value = "查询意见征集")
    @RequiresPermissions("shuo.feedback.read")
    @PutMapping(value = "/read/list")
    public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
        return super.query(modelMap, param);
    }

    @ApiOperation(value = "意见征集详情")
    @RequiresPermissions("shuo.feedback.read")
    @PutMapping(value = "/read/detail")
    public Object get(ModelMap modelMap, @RequestBody Feedback param) {
        return super.get(modelMap, param);
    }

    @PostMapping
    @ApiOperation(value = "修改意见征集")
    @RequiresPermissions("shuo.feedback.update")
    public Object update(ModelMap modelMap, @RequestBody Feedback param) {
        return super.update(modelMap, param);
    }

    @DeleteMapping
    @ApiOperation(value = "删除意见征集")
    @RequiresPermissions("shuo.feedback.delete")
    public Object delete(ModelMap modelMap, @RequestBody Feedback param) {
        return super.delete(modelMap, param);
    }
}
