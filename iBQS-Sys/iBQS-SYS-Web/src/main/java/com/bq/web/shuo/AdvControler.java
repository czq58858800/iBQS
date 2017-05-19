package com.bq.web.shuo;

import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.model.Album;
import com.bq.shuo.provider.IShuoProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 广告管理
 *
 * @author Harvey.Wei
 * @version 2016年4月2日 下午4:20:10
 */
@RestController
@Api(value = "广告管理", description = "广告管理")
@RequestMapping(value = "/shuo/banner")
public class AdvControler extends AbstractController<IShuoProvider> {
    @Override
    public String getService() {
        return "advService";
    }

    @ApiOperation(value = "查询广告")
    @RequiresPermissions("shuo.adv.read")
    @PutMapping(value = "/read/list")
    public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
        return super.query(modelMap, param);
    }

    @ApiOperation(value = "广告详情")
    @RequiresPermissions("shuo.adv.read")
    @PutMapping(value = "/read/detail")
    public Object get(ModelMap modelMap, @RequestBody Album param) {
        return super.get(modelMap, param);
    }

    @PostMapping
    @ApiOperation(value = "修改广告")
    @RequiresPermissions("shuo.adv.update")
    public Object update(ModelMap modelMap, @RequestBody Album param) {
        return super.update(modelMap, param);
    }

    @DeleteMapping
    @ApiOperation(value = "删除广告")
    @RequiresPermissions("shuo.adv.delete")
    public Object delete(ModelMap modelMap, @RequestBody Album param) {
        return super.delete(modelMap, param);
    }
}
