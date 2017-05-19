package com.bq.web.shuo;

import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.model.Report;
import com.bq.shuo.provider.IShuoProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 举报管理
 *
 * @author Harvey.Wei
 * @version 2016年4月2日 下午4:20:10
 */
@RestController
@Api(value = "举报管理", description = "举报管理")
@RequestMapping(value = "/shuo/report")
public class ReportControler extends AbstractController<IShuoProvider> {
    @Override
    public String getService() {
        return "reportService";
    }

    @ApiOperation(value = "查询举报")
    @RequiresPermissions("shuo.report.read")
    @PutMapping(value = "/read/list")
    public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
        return super.query(modelMap, param);
    }

    @ApiOperation(value = "举报详情")
    @RequiresPermissions("shuo.report.read")
    @PutMapping(value = "/read/detail")
    public Object get(ModelMap modelMap, @RequestBody Report param) {
        return super.get(modelMap, param);
    }

    @PostMapping
    @ApiOperation(value = "修改举报")
    @RequiresPermissions("shuo.report.update")
    public Object update(ModelMap modelMap, @RequestBody Report param) {
        return super.update(modelMap, param);
    }

    @DeleteMapping
    @ApiOperation(value = "删除举报")
    @RequiresPermissions("shuo.report.delete")
    public Object delete(ModelMap modelMap, @RequestBody Report param) {
        return super.delete(modelMap, param);
    }
}
