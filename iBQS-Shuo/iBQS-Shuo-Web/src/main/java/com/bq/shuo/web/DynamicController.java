package com.bq.shuo.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.support.HttpCode;
import com.bq.core.util.InstanceUtil;
import com.bq.core.util.WebUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.support.ForwardHelper;
import com.bq.shuo.support.SubjectHelper;
import com.bq.shuo.model.Dynamic;
import com.bq.shuo.model.Forward;
import com.bq.shuo.model.Subject;
import com.bq.shuo.provider.IShuoProvider;
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
 * DynamicController
 *
 * @author Harvey.Wei
 * @date 2016/11/16 0016
 */
@RestController
@Api(value = "用户动态接口", description = "用户动态接口")
@RequestMapping(value = "dynamic")
public class DynamicController extends AbstractController<IShuoProvider> {

    @Override
    public String getService() {
        return "dynamicService";
    }

    // 关注用户动态列表
    @ApiOperation(value = "关注用户动态列表")
    @GetMapping("/list")
    public Object list(HttpServletRequest request, ModelMap modelMap,
           @ApiParam(required = true, value = "页码") @RequestParam(value = "pageNum") Integer pageNum) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        params.put("userId",getCurrUser());
        params.put("currUserId",getCurrUser());

        List<String > followIds = (List<String>) provider.execute(new Parameter("userFollowingService","selectByUserIdFollows").setObjects(new Object[] {getCurrUser()})).getList();

        if (followIds.size() == 0) {
            return setModelMap(modelMap, HttpCode.NOT_DATA);
        }
        params.put("userIds",followIds.toArray(new String[followIds.size()]));
        Parameter queryBeansParam = new Parameter(getService(),"queryBeans").setMap(params);
        Page page = provider.execute(queryBeansParam).getPage();
        List<Map<String,Object>> resultList = InstanceUtil.newArrayList();
        for (Object obj:page.getRecords()) {
            Dynamic record = (Dynamic) obj;
            Subject subject = record.getSubject();
            Forward forward = record.getForward();
            if (subject != null) {
                Map<String,Object> resultMap = SubjectHelper.formatResultMap(subject);
                resultMap.put("dynType",1);
                resultList.add(resultMap);
            } else if (forward != null) {
                resultList.add(ForwardHelper.formatResultMap(forward));
            } else {
                Map<String,Object> resultMap = InstanceUtil.newHashMap();
                resultMap.put("uid",record.getValId());
                resultMap.put("dynType",record.getType());
            }
        }
        page.setRecords(resultList);
        return setSuccessModelMap(modelMap, page);
    }
}
