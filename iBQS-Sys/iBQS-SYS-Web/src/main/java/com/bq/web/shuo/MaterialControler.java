package com.bq.web.shuo;

import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.model.Material;
import com.bq.shuo.provider.IShuoProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 贴纸管理
 *
 * @author Harvey.Wei
 * @version 2016年4月2日 下午4:20:10
 */
@RestController
@Api(value = "贴纸管理", description = "贴纸管理")
@RequestMapping(value = "/shuo/material")
public class MaterialControler extends AbstractController<IShuoProvider> {
    @Override
    public String getService() {
        return "materialService";
    }

    @ApiOperation(value = "查询贴纸")
    @RequiresPermissions("shuo.material.read")
    @PutMapping(value = "/read/list")
    public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
        return super.query(modelMap, param);
    }

    @ApiOperation(value = "贴纸详情")
    @RequiresPermissions("shuo.material.read")
    @PutMapping(value = "/read/detail")
    public Object get(ModelMap modelMap, @RequestBody Material param) {
        return super.get(modelMap, param);
    }

    @PostMapping
    @ApiOperation(value = "修改贴纸")
    @RequiresPermissions("shuo.material.update")
    public Object update(ModelMap modelMap, @RequestBody Material param) {
        return super.update(modelMap, param);
    }

    @DeleteMapping
    @ApiOperation(value = "删除贴纸")
    @RequiresPermissions("shuo.material.delete")
    public Object delete(ModelMap modelMap, @RequestBody Material param) {
        return super.delete(modelMap, param);
    }
}
