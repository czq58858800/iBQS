package com.bq.shuo.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.Constants;
import com.bq.core.support.Assert;
import com.bq.core.support.HttpCode;
import com.bq.core.util.CacheUtil;
import com.bq.core.util.InstanceUtil;
import com.bq.core.util.WebUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.core.helper.PushType;
import com.bq.shuo.model.Notify;
import com.bq.shuo.provider.IShuoProvider;
import com.bq.shuo.support.NotifyHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/24.
 */
@RestController
@Api(value = "通知接口", description = "通知接口")
@RequestMapping(value = "notify")
public class NotifyController extends AbstractController<IShuoProvider> {
    public String getService() {
        return "notifyService";
    }

    @ApiOperation(value = "获取通知")
    @GetMapping(value = "list")
    public Object list(HttpServletRequest request, ModelMap modelMap,
                                 @ApiParam(required = false, value = "消息类型：（1:评论；2:@；3：转发；4：喜欢）") @RequestParam(value = "msgType",required = false) String msgType,
                                 @ApiParam(required = true, value = "分页") @RequestParam(value = "pageNum") Integer pageNum) {
        Assert.notNull(pageNum, "PAGE_NUM");
        Map<String, Object> params = WebUtil.getParameterMap(request);
        params.put("receiveUserId",getCurrUser());
        Parameter queryBeansParam = new Parameter("notifyService","queryBeans").setMap(params);
        Page page = provider.execute(queryBeansParam).getPage();

        String key = Constants.CACHE_NAMESPACE+Constants.CACHE_SHUO_NAMESPACE+"remind:"+getCurrUser();
        if (StringUtils.equals("1",msgType)) {
            CacheUtil.getCache().hdel(key,PushType.NOTIFY_COMMENTS_NUM);
        } else if (StringUtils.equals("2",msgType)) {
            CacheUtil.getCache().hdel(key,PushType.NOTIFY_AT_NUM);
        } else if (StringUtils.equals("3",msgType)) {
            CacheUtil.getCache().hdel(key,PushType.NOTIFY_FORWARD_NUM);
        } else if (StringUtils.equals("4",msgType)) {
            CacheUtil.getCache().hdel(key,PushType.NOTIFY_LIKED_NUM);
        }

        return  setSuccessModelMap(modelMap,NotifyHelper.formatResultPage(page));
    }

    @ApiOperation(value = "删除")
    @PostMapping(value = "delete")
    public Object delete(HttpServletRequest request, ModelMap modelMap,
                       @ApiParam(required = true, value = "通知ID") @RequestParam(value = "id") String id) {
        Assert.notNull(id, "ID");
        Parameter parameter = new Parameter("notifyService","queryById").setId(id);
        Notify record = (Notify) provider.execute(parameter).getModel();
        if (record == null || !StringUtils.equals(record.getReceiveUserId(),getCurrUser())) {
            return setModelMap(modelMap, HttpCode.UNAUTHORIZED,"无法删除别人的消息");
        }
        parameter = new Parameter("notifyService","delete").setId(id);
        provider.execute(parameter);
        return  setSuccessModelMap(modelMap);
    }

}
