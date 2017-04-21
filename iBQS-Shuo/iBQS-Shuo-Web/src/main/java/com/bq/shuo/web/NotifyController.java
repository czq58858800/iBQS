package com.bq.shuo.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.support.Assert;
import com.bq.core.support.HttpCode;
import com.bq.core.util.InstanceUtil;
import com.bq.core.util.WebUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
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
        page.setRecords(NotifyHelper.formatResultList(page.getRecords()));

        int unreadNum = (int) provider.execute(new Parameter("notifyService","getByUnreadNum").setMap(params)).getObject();
        modelMap.put("unreadNum",unreadNum);

        return  setSuccessModelMap(modelMap,page);
    }

    @ApiOperation(value = "删除")
    @PostMapping(value = "delete")
    public Object delete(HttpServletRequest request, ModelMap modelMap,
                       @ApiParam(required = true, value = "通知ID") @RequestParam(value = "id") String id) {
        Assert.notNull(id, "ID");
        Notify record = (Notify) provider.execute(new Parameter("notifyService","queryById").setId(id)).getModel();
        if (record == null || !StringUtils.equals(record.getReceiveUserId(),getCurrUser())) {
            return setModelMap(modelMap, HttpCode.UNAUTHORIZED,"无法删除别人的消息");
        }
        provider.execute(new Parameter(getService(),"delete").setId(id));
        return  setSuccessModelMap(modelMap);
    }

    @ApiOperation(value = "已读")
    @PostMapping(value = "read")
    public Object read(ModelMap modelMap,
                       @ApiParam(required = true,value = "通知ID") @RequestParam(value = "id") String id) {
        Assert.notNull(id, "ID");
        Notify record = (Notify) provider.execute(new Parameter("notifyService","queryById").setId(id)).getModel();
        record.setIsRead(true);
        provider.execute(new Parameter(getService(),"update").setModel(record));
        return setSuccessModelMap(modelMap);
    }

    @ApiOperation(value = "标记所有为已读")
    @PostMapping(value = "allRead")
    public Object allRead(ModelMap modelMap,
              @ApiParam(required = false, value = "消息类型：（1:评论；2:@；3：转发；4：喜欢）") @RequestParam(value = "t",required = false) String t) {
        provider.execute(new Parameter(getService(),"updateRead").setObjects(new Object[] {getCurrUser(),t}));
        return setSuccessModelMap(modelMap);
    }

}
