package com.bq.web.shuo;

import com.bq.core.util.PropertiesUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.BaseModel;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.model.Adv;
import com.bq.shuo.model.Album;
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
 * 广告管理
 *
 * @author Harvey.Wei
 * @version 2016年4月2日 下午4:20:10
 */
@RestController
@Api(value = "广告管理", description = "广告管理")
@RequestMapping(value = "/shuo/adv")
public class AdvControler extends AbstractController<IShuoProvider> {
    @Override
    public String getService() {
        return "advService";
    }

    @ApiOperation(value = "查询广告")
    @RequiresPermissions("shuo.adv.read")
    @PutMapping(value = "/read/list")
    public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
        return super.query(modelMap, param);
    }

    @ApiOperation(value = "广告详情")
    @RequiresPermissions("shuo.adv.read")
    @PutMapping(value = "/read/detail")
    public Object get(ModelMap modelMap, @RequestBody Adv param) {
        return super.get(modelMap, param);
    }

    @PostMapping
    @ApiOperation(value = "修改广告")
    @RequiresPermissions("shuo.adv.update")
    public Object update(ModelMap modelMap, @RequestBody Adv param) {
        return super.update(modelMap, param);
    }

    @DeleteMapping
    @ApiOperation(value = "删除广告")
    @RequiresPermissions("shuo.adv.delete")
    public Object delete(ModelMap modelMap, @RequestBody Adv param) {
        Parameter parameter = new Parameter(getService(), "queryById").setId(param.getId());
        logger.debug("{} execute queryById start...", parameter.getNo());
        Adv result = (Adv) provider.execute(parameter).getModel();
        logger.debug("{} execute queryById end.", parameter.getNo());

        Auth auth = Auth.create(PropertiesUtil.getString("qiniu.access_key"), PropertiesUtil.getString("qiniu.secret_key"));
        String key = result.getImage().replace(PropertiesUtil.getString("qiniu.domain"),"");
        String bucket = PropertiesUtil.getString("qiniu.bucket");
        Configuration cfg = new Configuration(Zone.zone0());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
            logger.debug("文件 {} 删除成功", key);
        } catch (QiniuException ex) {
            logger.error("文件 {} 删除失败 Code:{} Response:{}", key,ex.code(),ex.response.toString());
        }

        return super.delete(modelMap, param);
    }
}
