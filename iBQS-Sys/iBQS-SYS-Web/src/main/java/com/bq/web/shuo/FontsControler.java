package com.bq.web.shuo;

import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.model.Fonts;
import com.bq.shuo.provider.IShuoProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 字体管理
 *
 * @author Harvey.Wei
 * @version 2016年4月2日 下午4:20:10
 */
@RestController
@Api(value = "字体管理", description = "字体管理")
@RequestMapping(value = "/shuo/fonts")
public class FontsControler extends AbstractController<IShuoProvider> {
    @Override
    public String getService() {
        return "fontsService";
    }

    @ApiOperation(value = "查询字体")
    @RequiresPermissions("shuo.fonts.read")
    @PutMapping(value = "/read/list")
    public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
        return super.query(modelMap, param);
    }

    @ApiOperation(value = "字体详情")
    @RequiresPermissions("shuo.fonts.read")
    @PutMapping(value = "/read/detail")
    public Object get(ModelMap modelMap, @RequestBody Fonts param) {
        return super.get(modelMap, param);
    }

    @PostMapping
    @ApiOperation(value = "修改字体")
    @RequiresPermissions("shuo.fonts.update")
    public Object update(ModelMap modelMap, @RequestBody Fonts param) {
        return super.update(modelMap, param);
    }

    @DeleteMapping
    @ApiOperation(value = "删除字体")
    @RequiresPermissions("shuo.fonts.delete")
    public Object delete(ModelMap modelMap, @RequestBody Fonts param) {
        return super.delete(modelMap, param);
    }
}
