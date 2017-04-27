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
 * 专辑管理
 *
 * @author Harvey.Wei
 * @version 2016年4月2日 下午4:20:10
 */
@RestController
@Api(value = "专辑管理", description = "专辑管理")
@RequestMapping(value = "/shuo/album")
public class AlbumControler extends AbstractController<IShuoProvider> {
    @Override
    public String getService() {
        return "albumService";
    }

    @ApiOperation(value = "查询专辑")
    @RequiresPermissions("shuo.album.read")
    @PutMapping(value = "/read/list")
    public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
        return super.query(modelMap, param);
    }

    @ApiOperation(value = "专辑详情")
    @RequiresPermissions("shuo.album.read")
    @PutMapping(value = "/read/detail")
    public Object get(ModelMap modelMap, @RequestBody Album param) {
        return super.get(modelMap, param);
    }

    @PostMapping
    @ApiOperation(value = "修改专辑")
    @RequiresPermissions("shuo.album.update")
    public Object update(ModelMap modelMap, @RequestBody Album param) {
        return super.update(modelMap, param);
    }

    @DeleteMapping
    @ApiOperation(value = "删除专辑")
    @RequiresPermissions("shuo.album.delete")
    public Object delete(ModelMap modelMap, @RequestBody Album param) {
        return super.delete(modelMap, param);
    }
}
