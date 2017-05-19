package com.bq.web.shuo;

import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.model.MaterialHot;
import com.bq.shuo.provider.IShuoProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 素材热词管理
 *
 * @author Harvey.Wei
 * @version 2016年4月2日 下午4:20:10
 */
@RestController
@Api(value = "素材热词管理", description = "素材热词管理")
@RequestMapping(value = "/shuo/materialHot")
public class MaterialHotControler extends AbstractController<IShuoProvider> {
    @Override
    public String getService() {
        return "materialHotService";
    }

    @ApiOperation(value = "查询素材热词")
    @RequiresPermissions("shuo.material.hot.read")
    @PutMapping(value = "/read/list")
    public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
        return super.query(modelMap, param);
    }

    @ApiOperation(value = "素材热词详情")
    @RequiresPermissions("shuo.material.hot.read")
    @PutMapping(value = "/read/detail")
    public Object get(ModelMap modelMap, @RequestBody MaterialHot param) {
        return super.get(modelMap, param);
    }

    @PostMapping
    @ApiOperation(value = "修改素材热词")
    @RequiresPermissions("shuo.material.hot.update")
    public Object update(ModelMap modelMap, @RequestBody MaterialHot param) {
        return super.update(modelMap, param);
    }

    @DeleteMapping
    @ApiOperation(value = "删除素材热词")
    @RequiresPermissions("shuo.material.hot.delete")
    public Object delete(ModelMap modelMap, @RequestBody MaterialHot param) {
        return super.delete(modelMap, param);
    }
}
