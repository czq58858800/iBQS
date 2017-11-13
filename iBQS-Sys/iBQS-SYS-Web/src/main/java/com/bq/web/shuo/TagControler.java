package com.bq.web.shuo;

import com.bq.core.util.PropertiesUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.model.Tag;
import com.bq.shuo.provider.IShuoProvider;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 频道管理
 *
 * @author chern.zq
 * @version 2016年4月2日 下午4:20:10
 */
@RestController
@Api(value = "频道管理", description = "频道管理")
@RequestMapping(value = "/shuo/tag")
public class TagControler extends AbstractController<IShuoProvider> {
    @Override
    public String getService() {
        return "tagService";
    }

    @ApiOperation(value = "查询频道")
    @RequiresPermissions("shuo.tag.read")
    @PutMapping(value = "/read/list")
    public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
        return super.query(modelMap, param);
    }

    @ApiOperation(value = "频道详情")
    @RequiresPermissions("shuo.tag.read")
    @PutMapping(value = "/read/detail")
    public Object get(ModelMap modelMap, @RequestBody Tag param) {
        return super.get(modelMap, param);
    }

    @PostMapping
    @ApiOperation(value = "修改频道")
    @RequiresPermissions("shuo.tag.update")
    public Object update(ModelMap modelMap, @RequestBody Tag param) {
        return super.update(modelMap, param);
    }

    @DeleteMapping
    @ApiOperation(value = "删除频道")
    @RequiresPermissions("shuo.tag.delete")
    public Object delete(ModelMap modelMap, @RequestBody Tag param) {
        return super.delete(modelMap, param);
    }
}
