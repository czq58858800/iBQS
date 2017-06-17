package com.bq.web.shuo;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.util.InstanceUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.model.Subject;
import com.bq.shuo.model.Tag;
import com.bq.shuo.provider.IShuoProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 表情管理
 *
 * @author Harvey.Wei
 * @version 2016年4月2日 下午4:20:10
 */
@RestController
@Api(value = "表情管理", description = "表情管理")
@RequestMapping(value = "/shuo/subject")
public class SubjectControler extends AbstractController<IShuoProvider> {
    @Override
    public String getService() {
        return "subjectService";
    }

    @ApiOperation(value = "查询表情")
    @RequiresPermissions("shuo.subject.read")
    @PutMapping(value = "/read/list")
    public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
        String keyword = (String) param.get("keyword");
        if (StringUtils.isNotBlank(keyword)) {
            if (StringUtils.equals(keyword, "1") || StringUtils.equals("HOT",keyword.toUpperCase()) ) { // 热门主题
                param.remove("keyword");
                param.put("orderHot",true);
            } else if (StringUtils.equals(keyword,"0") || StringUtils.equals("NEW",keyword.toUpperCase())){
                param.remove("keyword");
            }
        }

        Parameter parameter = new Parameter(getService(), "queryBeans").setMap(param);
        logger.debug("{} execute query start...", parameter.getNo());
        Page<Subject> list = (Page<Subject>) provider.execute(parameter).getPage();
        logger.debug("{} execute query end.", parameter.getNo());

        parameter = new Parameter("tagService","queryByAll");
        List<Tag> tagList = (List<Tag>) provider.execute(parameter).getList();
        Map<String,Object> extendMap = InstanceUtil.newHashMap();
        extendMap.put("tags",tagList);
        return setSuccessModelMap(modelMap, list,extendMap);
    }

    @ApiOperation(value = "表情详情")
    @RequiresPermissions("shuo.subject.read")
    @PutMapping(value = "/read/detail")
    public Object get(ModelMap modelMap, @RequestBody Subject param) {
        return super.get(modelMap, param);
    }

    @PostMapping
    @ApiOperation(value = "修改表情")
    @RequiresPermissions("shuo.subject.update")
    public Object update(ModelMap modelMap, @RequestBody Subject param) {
        return super.update(modelMap, param);
    }

    @ApiOperation(value = "删除表情")
    @RequiresPermissions("shuo.subject.delete")
    @PostMapping(value = "/delete")
    public Object delete(ModelMap modelMap, @RequestBody Subject param) {
        return super.delete(modelMap, param);
    }


    @ApiOperation(value = "推荐热门表情")
    @RequiresPermissions("shuo.subject.update")
    @PostMapping(value = "/updateHot")
    public Object updateHot(ModelMap modelMap, @RequestBody Subject param) {
        Parameter parameter = new Parameter(getService(), "queryById").setId(param.getId());
        logger.debug("{} execute queryById start...", parameter.getNo());
        Subject record = (Subject) provider.execute(parameter).getModel();
        logger.debug("{} execute queryById end.", parameter.getNo());
        record.setIsHot(param.getIsHot());
        if (param.getIsHot()) {
            record.setHotTime(new Date());
        } else {
            record.setHotTime(null);
        }

        parameter = new Parameter(getService(), "update").setModel(param);
        logger.debug("{} execute update start...", parameter.getNo());
        provider.execute(parameter);
        logger.debug("{} execute update end.", parameter.getNo());
        return setSuccessModelMap(modelMap);
    }
}
