package com.bq.web.shuo;

import com.bq.core.util.PropertiesUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.model.Category;
import com.bq.shuo.model.Material;
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

import java.util.List;
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

    @PostMapping(value = "/update")
    @ApiOperation(value = "修改贴纸")
    @RequiresPermissions("shuo.material.update")
    public Object update(ModelMap modelMap, @RequestBody Material param) {
        return super.update(modelMap, param);
    }

    @PostMapping(value = "/delete")
    @ApiOperation(value = "删除贴纸")
    @RequiresPermissions("shuo.material.delete")
    public Object delete(ModelMap modelMap, @RequestBody Material param) {
        Parameter parameter = new Parameter(getService(), "queryById").setId(param.getId());
        logger.debug("{} execute queryById start...", parameter.getNo());
        Material result = (Material) provider.execute(parameter).getModel();
        logger.debug("{} execute queryById end.", parameter.getNo());

        Auth auth = Auth.create(PropertiesUtil.getString("qiniu.access_key"), PropertiesUtil.getString("qiniu.secret_key"));
        String bucket = PropertiesUtil.getString("qiniu.bucket");
        Configuration cfg = new Configuration(Zone.zone0());
        BucketManager bucketManager = new BucketManager(auth, cfg);

        String key = result.getImage().replace(PropertiesUtil.getString("qiniu.domain"),"");
        try {
            bucketManager.delete(bucket, key);
            logger.debug("文件 {} 删除成功", key);
        } catch (QiniuException ex) {
            logger.error("文件 {} 删除失败 Code:{} Response:{}", key,ex.code(),ex.response.toString());
        }

        parameter = new Parameter("categoryService","queryById").setId(param.getCategoryId());
        Category category = (Category) provider.execute(parameter).getModel();
        category.setStuffNum(category.getStuffNum()-1);
        parameter = new Parameter("categoryService","update").setModel(category);
        provider.execute(parameter);

        return super.delete(modelMap, param);
    }


    @PostMapping(value = "batchDelete")
    @ApiOperation(value = "删除贴纸")
    @RequiresPermissions("shuo.material.delete")
    public Object batchDelete(ModelMap modelMap, @RequestBody Material param) {
        Parameter parameter = new Parameter(getService(), "selectAllByCategoryId").setId(param.getId());
        logger.debug("{} execute queryById start...", parameter.getNo());
        List<Material> materialList = (List<Material>) provider.execute(parameter).getList();
        logger.debug("{} execute queryById end.", parameter.getNo());

        Auth auth = Auth.create(PropertiesUtil.getString("qiniu.access_key"), PropertiesUtil.getString("qiniu.secret_key"));
        String bucket = PropertiesUtil.getString("qiniu.bucket");
        Configuration cfg = new Configuration(Zone.zone0());
        BucketManager bucketManager = new BucketManager(auth, cfg);


        for (Material result:materialList) {
            String key = result.getImage().replace(PropertiesUtil.getString("qiniu.domain"),"");
            try {
                bucketManager.delete(bucket, key);
                logger.debug("文件 {} 删除成功", key);

                parameter = new Parameter(getService(), "delete").setId(result.getId());
                logger.debug("{} execute delete start...", parameter.getNo());
                provider.execute(parameter);
                logger.debug("{} execute delete end.", parameter.getNo());

            } catch (QiniuException ex) {
                logger.error("文件 {} 删除失败 Code:{} Response:{}", key,ex.code(),ex.response.toString());
            }
        }
        parameter = new Parameter("categoryService","queryById").setId(param.getId());
        Category category = (Category) provider.execute(parameter).getModel();
        category.setStuffNum(0);
        parameter = new Parameter("categoryService","update").setModel(category);
        provider.execute(parameter);

        return setSuccessModelMap(modelMap);
    }
}
