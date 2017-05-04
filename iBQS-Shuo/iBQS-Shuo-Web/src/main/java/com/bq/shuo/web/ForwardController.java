package com.bq.shuo.web;

import com.bq.core.support.HttpCode;
import com.bq.core.util.Request2ModelUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.core.helper.PushType;
import com.bq.shuo.core.util.Push;
import com.bq.shuo.model.*;
import com.bq.shuo.provider.IShuoProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * ForwardController
 *
 * @author Harvey.Wei
 * @date 2016/11/15 0015
 */
@RestController
@Api(value = "转发接口", description = "转发接口")
@RequestMapping(value = "forward")
public class ForwardController extends AbstractController<IShuoProvider> {

    @Override
    public String getService() {
        return "forwardService";
    }

    /**
     * 转发主题
     * @param request
     * @param modelMap
     * @return
     */
    @ApiOperation(value = "转发主题")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object add(HttpServletRequest request, ModelMap modelMap,
                      @ApiParam(required = true, value = "主题ID") @RequestParam(value = "subjectId") String subjectId,
                      @ApiParam(required = true, value = "内容") @RequestParam(value = "content") String content) {

        Forward record = Request2ModelUtil.covert(Forward.class, request);
        record.setUserId(getCurrUser());


        Subject subject = (Subject) provider.execute(new Parameter("subjectService","queryById").setId(subjectId)).getModel();


        Parameter parameter = new Parameter("blacklistService","selectIsBlacklistByUserId").setObjects(new Object[] {subject.getUserId(),getCurrUser()});
        boolean isBlacklist = (boolean) provider.execute(parameter).getObject();
        if (isBlacklist) {
            return setModelMap(modelMap, HttpCode.ALREADY_IN_BLACKLIST);
        }

        record = (Forward) provider.execute(new Parameter(getService(),"update").setModel(record)).getModel();

        Dynamic dynamic = new Dynamic();
        dynamic.setUserId(getCurrUser());
        dynamic.setValId(record.getId());
        dynamic.setType("2");
        dynamic.setEnable(true);
        parameter = new Parameter("dynamicService","update").setModel(dynamic);
        provider.execute(parameter);

        // 转发推送
        new Push(PushType.FORWARD,getCurrUser(),subject.getUserId(),subjectId,content);

        return setSuccessModelMap(modelMap);
    }

    // 删除转发主题
    @ApiOperation(value = "删除转发主题")
    @PostMapping(value = "/delete")
    public Object delete(HttpServletRequest request, ModelMap modelMap,
                         @ApiParam(required = true, value = "转发ID")@RequestParam(value = "id") String id) {

        Forward record = (Forward) provider.execute(new Parameter(getService(),"queryById").setId(id)).getModel();

        if (record != null) {
            if (!StringUtils.equals(record.getUserId(), getCurrUser())) {
                // 当前登录用户ID与转发主题的用户ID不一致
                return setModelMap(modelMap, HttpCode.UNAUTHORIZED);
            }
            provider.execute(new Parameter(getService(),"delete").setId(id));
            return setSuccessModelMap(modelMap);
        }
        return setModelMap(modelMap,HttpCode.NOT_DATA);
    }
}
