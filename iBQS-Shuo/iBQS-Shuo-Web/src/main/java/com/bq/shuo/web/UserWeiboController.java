package com.bq.shuo.web;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.support.Assert;
import com.bq.core.support.HttpCode;
import com.bq.core.util.InstanceUtil;
import com.bq.core.util.WebUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.model.User;
import com.bq.shuo.model.UserThirdparty;
import com.bq.shuo.model.UserWeibo;
import com.bq.shuo.provider.IShuoProvider;
import com.bq.shuo.support.UserHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/1/12.
 */
@RestController
@Api(value = "用户微博好友接口", description = "用户微博好友接口")
@RequestMapping(value = "user/weibo")
public class UserWeiboController extends AbstractController<IShuoProvider> {
    @Override
    public String getService() {
        return "userWeiboService";
    }

/*
    // 获取微博好友列表
    @ApiOperation(value = "获取微博好友列表")
    @GetMapping("/list")
    public Object list(HttpServletRequest request, ModelMap modelMap,
                       @ApiParam(required = true, value = "状态：(1:已关注;2:未关注;3:未注册)") @RequestParam(value = "status") String status,
                       @ApiParam(required = true, value = "标记：(true:立即更新微博数据，false:后台更新)") @RequestParam(value = "flag") boolean flag,
                       @ApiParam(required = true, value = "分页") @RequestParam(value = "pageNum") Integer pageNum) {
        Assert.notNull(status, "STATUS");
        Assert.notNull(flag, "FLAG");
        Assert.notNull(pageNum, "PAGE_NUM");

        Map<String, Object> params = WebUtil.getParameterMap(request);

        if (flag) {
            userWeiboService.updateWeibo(getCurrUser());
        }

        params.put("userId",getCurrUser());

        Page pageInfo = userWeiboService.queryBeans(params);

        List<Map<String,Object>> resultList = InstanceUtil.newArrayList();
        for (Object obj:pageInfo.getRecords()) {
            UserWeibo record = (UserWeibo) obj;
            Map<String,Object> itemMap = InstanceUtil.newHashMap();

            if (StringUtils.equals(status,"1") || StringUtils.equals(status,"2")) {
                UserThirdparty thirdparty = thirdPartyService.queryUserIdByThirdParty(record.getOpenId(),"SINA");
                if (thirdparty !=null) {
                    User user = userService.queryById(thirdparty.getUserId());
                    resultList.add(UserHelper.formatResultMap(user));
                } else {
                    return setModelMap(modelMap, HttpCode.NOT_DATA);
                }
            } else {
                itemMap.put("uid",record.getId());
                itemMap.put("openId",record.getOpenId());
                itemMap.put("avatar",record.getAvatar());
                itemMap.put("name",record.getName());
                itemMap.put("gender",record.getGender());
                itemMap.put("isInvite",record.getIsInvite());
                itemMap.put("verified",record.getVerified());
                itemMap.put("summary",record.getSummary());
                resultList.add(itemMap);
            }
        }
        pageInfo.setRecords(resultList);
        return setSuccessModelMap(modelMap,pageInfo);
    }

    // 获取微博好友列表
    @ApiOperation(value = "data")
    @GetMapping("/invite")
    public Object invite(HttpServletRequest request, ModelMap modelMap,
                       @ApiParam(required = true, value = "数据:(['id','id',...,'id'])") @RequestParam(value = "data") String data) {
        JSONArray dataJson = JSONArray.parseArray(data);

        List<UserWeibo> resultList = InstanceUtil.newArrayList();
        for (Object obj:dataJson) {
            String id = (String) obj;
            UserWeibo weiboBean = userWeiboService.queryById(id);
            if (weiboBean != null && weiboBean.getIsInvite()) {
                resultList.add(weiboBean);
            }
        }

        WeiboInvite record = new WeiboInviteBean();
        record.setInvites(resultList);

        UserThirdparty userThirdparty = thirdPartyService.queryThirdPartyByUserId(getCurrUser(),"SINA");
        if (userThirdparty == null) {
            return setModelMap(modelMap,HttpCode.USER_NON_EXISTENT);
        }

        record.setOpenId(userThirdparty.getOpenId());
        record.setThirdpartyId(userThirdparty.getId());
        record.setToken(userThirdparty.getToken());
        record.setUserId(getCurrUser());

        sendWeiboInvite(record);

        return setSuccessModelMap(modelMap);
    }


    private void sendWeiboInvite(final WeiboInvite record) {
        executorService.submit(new Runnable() {
            public void run() {
                queueSender.send("iBQS.weiboInviteSender", record);
            }
        });
    }*/

}
