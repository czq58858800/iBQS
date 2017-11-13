package com.bq.shuo.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.support.Assert;
import com.bq.core.util.WebUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.model.Adv;
import com.bq.shuo.provider.IShuoProvider;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 广告接口
 *
 * @author chern.zq
 * @version 2016年4月2日 下午4:20:10
 */
@RestController
@Api(value = "广告接口", description = "广告接口")
@RequestMapping(value = "/banner")
public class AdvController extends AbstractController<IShuoProvider> {
    @Override
    public String getService() {
        return "advService";
    }

    /**
     * 获取广告列表
     * @param request
     * @param modelMap
     * @param type 广告类型：
 *              1：发现；
 *              2：热门素材；
 *              3：新品素材；
     * @return
     */
    @GetMapping(value = "list")
    @ApiOperation(value = "广告列表")
    public Object list(HttpServletRequest request, ModelMap modelMap,
                 @ApiParam(value = "广告类型：(1:话题;2:贴纸;)") @RequestParam(value = "type") String type) {
        Assert.notNull(type, "TYPE");
        Map<String, Object> params = WebUtil.getParameterMap(request);
        Parameter shuoParameter = new Parameter(getService(), "query").setMap(params);
        Page pageInfo = provider.execute(shuoParameter).getPage();
        List<Map<String,Object>> resultList = Lists.newArrayList();
        for (Object obj:pageInfo.getRecords()) {
            Adv record = (Adv) obj;
            Map<String,Object> resultMap = Maps.newHashMap();
            resultMap.put("uid",record.getId());
            resultMap.put("name",record.getName());
            resultMap.put("url",record.getUrl());
            resultMap.put("sortNo",record.getSortNo());
            resultMap.put("image",record.getImage());
            resultMap.put("type",record.getType());
            resultMap.put("createTime",record.getCreateTime().getTime());
            resultList.add(resultMap);
        }
        return setSuccessModelMap(modelMap,resultList);
    }
}
