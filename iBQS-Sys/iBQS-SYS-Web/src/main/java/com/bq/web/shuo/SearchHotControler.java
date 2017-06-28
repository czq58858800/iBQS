package com.bq.web.shuo;

import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.model.SearchHot;
import com.bq.shuo.provider.IShuoProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 搜索热词管理
 *
 * @author Harvey.Wei
 * @version 2016年4月2日 下午4:20:10
 */
@RestController
@Api(value = "搜索热词管理", description = "搜索热词管理")
@RequestMapping(value = "/shuo/searchHot")
public class SearchHotControler extends AbstractController<IShuoProvider> {
    @Override
    public String getService() {
        return "searchHotService";
    }

    @ApiOperation(value = "查询搜索热词")
    @RequiresPermissions("shuo.search.hot.read")
    @PutMapping(value = "/read/list")
    public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
        return super.query(modelMap, param);
    }

    @ApiOperation(value = "搜索热词详情")
    @RequiresPermissions("shuo.search.hot.read")
    @PutMapping(value = "/read/detail")
    public Object get(ModelMap modelMap, @RequestBody SearchHot param) {
        return super.get(modelMap, param);
    }

    @ApiOperation(value = "修改搜索热词")
    @RequiresPermissions("shuo.search.hot.update")
    @PostMapping(value = "/update")
    public Object update(ModelMap modelMap, @RequestBody SearchHot param) {
        return super.update(modelMap, param);
    }

    @DeleteMapping
    @ApiOperation(value = "删除搜索热词")
    @RequiresPermissions("shuo.search.hot.delete")
    public Object delete(ModelMap modelMap, @RequestBody SearchHot param) {
        return super.delete(modelMap, param);
    }
}
