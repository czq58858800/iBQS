package com.bq.shuo.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.util.InstanceUtil;
import com.bq.core.util.WebUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.model.Subject;
import com.bq.shuo.support.SubjectHelper;
import com.bq.shuo.model.Tag;
import com.bq.shuo.provider.IShuoProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * IndexController
 *
 * @author chern.zq
 * @date 2016/10/7 0007
 */
@RestController
@Api(value = "首页接口", description = "首页接口")
public class IndexController extends AbstractController<IShuoProvider> {

    @Override
    public String getService() {
        return "tagService";
    }

    // 主题列表
    @ApiOperation(value = "首页")
    @GetMapping(value = {"/tag/index"})
    public Object list(HttpServletRequest request,ModelMap modelMap,
                       @ApiParam(value = "标签获取条数") @RequestParam(value = "pageSize",required = false) Integer pageSize,
                       @ApiParam(value = "热门获取条数") @RequestParam(value = "hotPageSize",required = false) Integer hotPageSize,
                       @ApiParam(value = "其他获取条数") @RequestParam(value = "otherPageSize",required = false) Integer otherPageSize,
                       @ApiParam(required = true, value = "页码") @RequestParam(value = "pageNum") Integer pageNum) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        if (pageSize == null || pageSize == 0)
            pageSize = 2;
        if (hotPageSize == null || hotPageSize == 0)
            hotPageSize = 12;
        if (otherPageSize == null || otherPageSize == 0)
            pageSize = 8;

        params.put("currUserId",getCurrUser());
        params.put("pageSize",pageSize);
        params.put("enable",true);
        params.put("blankNew",true);

        Parameter querParam = new Parameter(getService(),"query").setMap(params);
        Page page = provider.execute(querParam).getPage();

        List resultList = InstanceUtil.newArrayList();
        for (Object object:page.getRecords()) {
            Tag tag = (Tag) object;
            Map<String,Object> resultMap = InstanceUtil.newHashMap();
            resultMap.put("uid",tag.getId());
            resultMap.put("name",tag.getName());
            resultMap.put("code",tag.getCode());
            if (tag.getCode() != null) {
                params.put("orderHot",true);
                params.put("pageSize",hotPageSize);
                Parameter parameter = new Parameter("subjectService","queryByHot").setMap(params);
                resultMap.put("subject", SubjectHelper.formatBriefResultList(provider.execute(parameter).getPage().getRecords()));
            } else {
                params.remove("orderHot");
                params.put("keyword",tag.getName());
                params.put("pageSize",otherPageSize);
                Parameter parameter = new Parameter("subjectService","queryByNew").setMap(params);
                resultMap.put("subject", SubjectHelper.formatBriefResultList(provider.execute(parameter).getPage().getRecords()));
            }
            resultList.add(resultMap);
        }
        page.setRecords(resultList);
        return setSuccessModelMap(modelMap,page);
    }
}
