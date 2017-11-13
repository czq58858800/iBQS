package com.bq.web.shuo;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.util.PropertiesUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.core.util.QiniuUtil;
import com.bq.shuo.model.Adv;
import com.bq.shuo.model.Subject;
import com.bq.shuo.model.Topics;
import com.bq.shuo.model.TopicsReview;
import com.bq.shuo.provider.IShuoProvider;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * 话题管理
 *
 * @author chern.zq
 * @version 2016年4月2日 下午4:20:10
 */
@RestController
@Api(value = "话题主持人管理", description = "话题主持人管理")
@RequestMapping(value = "/shuo/topic/review")
public class TopicReviewControler extends AbstractController<IShuoProvider> {
    @Override
    public String getService() {
        return "topicsReviewService";
    }


    @ApiOperation(value = "查询话题主持人")
    @RequiresPermissions("shuo.topic.review.read")
    @PutMapping(value = "/read/list")
    public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
        Parameter parameter = new Parameter(getService(), "queryBeans").setMap(param);
        logger.debug("{} execute query start...", parameter.getNo());
        Page<?> list = provider.execute(parameter).getPage();
        logger.debug("{} execute query end.", parameter.getNo());
        return setSuccessModelMap(modelMap, list);
    }

    @ApiOperation(value = "话题主持人详情")
    @RequiresPermissions("shuo.topic.review.read")
    @PutMapping(value = "/read/detail")
    public Object get(ModelMap modelMap, @RequestBody TopicsReview param) {
        return super.get(modelMap, param);
    }


    @ApiOperation(value = "修改话题主持人")
    @RequiresPermissions("shuo.topic.review.update")
    @PostMapping("/update")
    public Object update(ModelMap modelMap, @RequestBody TopicsReview param) {
        return super.update(modelMap, param);
    }

    @DeleteMapping
    @ApiOperation(value = "删除话题主持人")
    @RequiresPermissions("shuo.topic.review.delete")
    public Object delete(ModelMap modelMap, @RequestBody TopicsReview param) {
        Parameter parameter = new Parameter(getService(), "queryById").setId(param.getId());
        logger.debug("{} execute queryById start...", parameter.getNo());
        TopicsReview result = (TopicsReview) provider.execute(parameter).getModel();
        logger.debug("{} execute queryById end.", parameter.getNo());

        parameter = new Parameter("topicsService", "queryById").setId(param.getId());
        logger.debug("{} Topics execute queryById start...", parameter.getNo());
        Topics topics = (Topics) provider.execute(parameter).getModel();
        logger.debug("{} Topics execute queryById end.", parameter.getNo());

        if (!StringUtils.equals(topics.getCover(),result.getCover())) {
            removeImage(result.getCover());
        }

        if (!StringUtils.equals(topics.getBanner(),result.getBanner())) {
            removeImage(result.getBanner());
        }

        return super.delete(modelMap, param);
    }

    private void removeImage(String image) {
        if (StringUtils.isNotBlank(image)) {
            Auth auth = Auth.create(PropertiesUtil.getString("qiniu.access_key"), PropertiesUtil.getString("qiniu.secret_key"));
            String bucket = PropertiesUtil.getString("qiniu.bucket");
            Configuration cfg = new Configuration(Zone.zone0());
            BucketManager bucketManager = new BucketManager(auth, cfg);
            String key = image.replace(PropertiesUtil.getString("qiniu.domain"), "");
            try {
                bucketManager.delete(bucket, key);
                logger.debug("文件 {} 删除成功", key);
            } catch (QiniuException ex) {
                logger.error("文件 {} 删除失败 Code:{} Response:{}", key, ex.code(), ex.response.toString());
            }
        }
    }

    @ApiOperation(value = "修改话题主持人")
    @RequiresPermissions("shuo.topic.review.update")
    @PostMapping("/updateAudit")
    public Object updateAudit(ModelMap modelMap, @RequestBody TopicsReview param) {
        if (StringUtils.isNotBlank(param.getId())) {
            param.setUpdateTime(new Date());
            Parameter parameter = new Parameter(getService(), "update").setModel(param);
            logger.debug("{} execute update start...", parameter.getNo());
            provider.execute(parameter);
            logger.debug("{} execute update end.", parameter.getNo());

            parameter = new Parameter(getService(),"queryById").setId(param.getId());
            TopicsReview topicsReview = (TopicsReview) provider.execute(parameter).getModel();

            String audit = param.getAudit();
            if (StringUtils.equals(audit,"2")) {
                Topics topics = new Topics();
                topics.setId(topicsReview.getTopicId());
                topics.setBanner(topicsReview.getBanner());
                topics.setSummary(topicsReview.getSummary());
                topics.setOwnerId(topicsReview.getOwnerId());
                topics.setAudit("2");
                String cover = topicsReview.getCover();
                if (StringUtils.isNotBlank(cover)) {
                    topics.setCover(cover);
                    JSONObject imageInfo = QiniuUtil.getImageInfo(cover);
                    if (imageInfo.containsKey("format")) {
                        topics.setCoverType(imageInfo.getString("format"));
                        topics.setCoverWidth(imageInfo.getInteger("width"));
                        topics.setCoverHeight(imageInfo.getInteger("height"));
                    }
                }

                parameter = new Parameter("topicsService", "update").setModel(topics);
                provider.execute(parameter);
            }
        }
        return setSuccessModelMap(modelMap);
    }


}
