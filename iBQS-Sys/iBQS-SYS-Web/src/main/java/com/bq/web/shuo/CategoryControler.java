package com.bq.web.shuo;

import com.bq.core.support.HttpCode;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.model.Category;
import com.bq.shuo.provider.IShuoProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 贴纸分类管理
 *
 * @author Harvey.Wei
 * @version 2016年4月2日 下午4:20:10
 */
@RestController
@Api(value = "贴纸分类管理", description = "贴纸分类管理")
@RequestMapping(value = "/shuo/category")
public class CategoryControler extends AbstractController<IShuoProvider> {
    @Override
    public String getService() {
        return "categoryService";
    }

    @ApiOperation(value = "查询贴纸分类")
    @RequiresPermissions("shuo.material.read")
    @PutMapping(value = "/read/list")
    public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
        return super.query(modelMap, param);
    }

    @ApiOperation(value = "贴纸分类详情")
    @RequiresPermissions("shuo.material.read")
    @PutMapping(value = "/read/detail")
    public Object get(ModelMap modelMap, @RequestBody Category param) {
        return super.get(modelMap, param);
    }

    @PostMapping(value = "update")
    @ApiOperation(value = "修改贴纸分类")
    @RequiresPermissions("shuo.material.update")
    public Object update(ModelMap modelMap, @RequestBody Category param) {
        return super.update(modelMap, param);
    }

    @PostMapping(value = "delete")
    @ApiOperation(value = "删除贴纸分类")
    @RequiresPermissions("shuo.material.delete")
    public Object delete(ModelMap modelMap, @RequestBody Category param) {
        Parameter parameter = new Parameter("materialService","selectCountByCategory").setId(param.getId());
        int materialCount = (int) provider.execute(parameter).getObject();
        if (materialCount != 0) {
            return setModelMap(modelMap, HttpCode.NOT_DATA);
        }
        return super.delete(modelMap, param);
    }
}
