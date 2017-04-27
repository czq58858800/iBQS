package com.bq.web.shuo;

import com.bq.core.util.InstanceUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.BaseModel;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.model.User;
import com.bq.shuo.provider.IShuoProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户管理
 *
 * @author Harvey.Wei
 * @version 2016年4月2日 下午4:20:10
 */
@RestController
@Api(value = "用户管理", description = "用户管理")
@RequestMapping(value = "/shuo/user")
public class UserControler extends AbstractController<IShuoProvider> {
    @Override
    public String getService() {
        return "userService";
    }

    @ApiOperation(value = "查询用户")
    @RequiresPermissions("shuo.user.read")
    @PutMapping(value = "/read/list")
    public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
        return super.query(modelMap, param);
    }

    @ApiOperation(value = "用户详情")
    @RequiresPermissions("shuo.user.read")
    @PutMapping(value = "/read/detail")
    public Object get(ModelMap modelMap, @RequestBody User param) {
        return super.get(modelMap, param);
    }

    @PostMapping
    @ApiOperation(value = "修改用户")
    @RequiresPermissions("shuo.user.update")
    public Object update(ModelMap modelMap, @RequestBody User param) {
        return super.update(modelMap, param);
    }

    @DeleteMapping
    @ApiOperation(value = "删除用户")
    @RequiresPermissions("shuo.user.delete")
    public Object delete(ModelMap modelMap, @RequestBody User param) {
        return super.delete(modelMap, param);
    }

    @ApiOperation(value = "用户简略资料")
    @PutMapping(value = "/read/brief")
    public Object brief(ModelMap modelMap, @RequestBody User param) {
        Parameter parameter = new Parameter(getService(), "queryById").setId(param.getId());
        logger.info("{} execute queryById start...", parameter.getNo());
        User result = (User) provider.execute(parameter).getModel();
        logger.info("{} execute queryById end.", parameter.getNo());

        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        resultMap.put("id",result.getId());
        resultMap.put("avatar",result.getAvatar());
        resultMap.put("name",result.getName());
        resultMap.put("sex",result.getSex());
        resultMap.put("summary",result.getSummary());
        resultMap.put("userType",result.getUserType());
        return setSuccessModelMap(modelMap, resultMap);
    }
}
