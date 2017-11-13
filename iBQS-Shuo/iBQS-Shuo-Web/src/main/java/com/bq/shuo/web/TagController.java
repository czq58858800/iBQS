package com.bq.shuo.web;

import com.bq.core.util.InstanceUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.model.Tag;
import com.bq.shuo.provider.IShuoProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * TagController
 *
 * @author chern.zq
 * @date 2016/10/10 0010
 */
@RestController
@Api(value = "标签接口", description = "标签接口")
@RequestMapping(value = "tag")
public class TagController extends AbstractController<IShuoProvider> {
    @Override
    public String getService() {
        return "tagService";
    }

    /**
     * 标签列表
     * @param modelMap
     * @return
     */
    @ApiOperation(value = "标签列表")
    @GetMapping("/list")
    public Object list(ModelMap modelMap) {
        List<Map<String,Object>> resultList = InstanceUtil.newArrayList();

        Parameter queryByAllParam = new Parameter("tagService","queryByAll");
        List<Tag> tags = (List<Tag>) provider.execute(queryByAllParam).getList();

        for (Tag tag : tags) {
            Map<String,Object> resultMap = InstanceUtil.newHashMap();
            resultMap.put("uid", tag.getId());
            resultMap.put("name", tag.getName());
            resultMap.put("code", tag.getCode());
            resultMap.put("type", tag.getType());
            resultList.add(resultMap);
        }
        return setSuccessModelMap(modelMap, resultList);
    }

}
