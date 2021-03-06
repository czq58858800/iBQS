package com.bq.shuo.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.config.Resources;
import com.bq.core.support.Assert;
import com.bq.core.support.HttpCode;
import com.bq.core.util.HttpUtil;
import com.bq.core.util.InstanceUtil;
import com.bq.core.util.WebUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.core.util.WeiboHelper;
import com.bq.shuo.model.UserThirdparty;
import com.bq.shuo.model.UserWeibo;
import com.bq.shuo.model.ext.WeiboInvite;
import com.bq.shuo.provider.IShuoProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
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

    // 线程池
    private ExecutorService executorService = Executors.newCachedThreadPool();

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
            updateWeibo(getCurrUser());
        } else {
          if (pageNum == 1) {
              executorService.submit(new Runnable() {
                 public void run() {
                     updateWeibo(getCurrUser());
                 }
              });
          }
        }


        if (StringUtils.equals(status,"1")) {
            params.put("follow",true);
        } else if (StringUtils.equals(status,"2")){
            params.put("follow",false);
        }else {
            params.put("unregistered",true);
        }
        params.put("userId",getCurrUser());
        Parameter parameter = new Parameter(getService(),"queryFollow").setMap(params);
        Page page = provider.execute(parameter).getPage();
        List<Map<String,Object>> resultList = InstanceUtil.newArrayList();
        for (Object obj:page.getRecords()) {
            UserWeibo record = (UserWeibo) obj;
            Map<String,Object> itemMap = InstanceUtil.newHashMap();
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
        page.setRecords(resultList);
        return setSuccessModelMap(modelMap,page);
    }

    // 获取微博好友列表
    @ApiOperation(value = "data")
    @GetMapping("/invite")
    public Object invite(HttpServletRequest request, ModelMap modelMap,
                       @ApiParam(required = true, value = "数据:(['id','id',...,'id'])") @RequestParam(value = "data") String data) {
        JSONArray dataJson = JSONArray.parseArray(data);


        Parameter parameter = null;
        List<UserWeibo> resultList = InstanceUtil.newArrayList();
        for (Object obj:dataJson) {
            String id = (String) obj;
            parameter = new Parameter(getService(),"queryById").setId(id);
            UserWeibo weiboBean = (UserWeibo) provider.execute(parameter).getModel();
            if (weiboBean != null && weiboBean.getIsInvite()) {
                resultList.add(weiboBean);
            }
        }

        WeiboInvite record = new WeiboInvite();
        record.setInvites(resultList);

        parameter = new Parameter("userThirdpartyService","queryThirdPartyByUserId").setObjects(new Object[] {getCurrUser(),"SINA"});
        UserThirdparty userThirdparty = (UserThirdparty) provider.execute(parameter).getModel();
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

    public void updateWeibo(String userId) {
        Parameter parameter = new Parameter("userThirdpartyService","queryThirdPartyByUserId").setObjects(new Object[] {userId,"SINA"});
        UserThirdparty thirdparty = (UserThirdparty) provider.execute(parameter).getModel();
        int nextCursor = 0;
        int wbPageNum = 200;
        String url = Resources.THIRDPARTY.getString("getFriendsURL_sina");
        String params = "?access_token=" + thirdparty.getToken() + "&uid=" + thirdparty.getOpenId()+"&count="+wbPageNum+"&cursor="+nextCursor;
        String res = HttpUtil.httpClientPost(url+params);
        JSONObject json = JSONObject.parseObject(res);
        if (!json.containsKey("error_code")) {
            Map<String,Object> weiboParams = InstanceUtil.newHashMap();
            nextCursor = json.getInteger("next_cursor");
            boolean flag = true;
            while (flag) {
                JSONArray jsonArray = json.getJSONArray("users");
                for (Object obj : jsonArray) {
                    JSONObject o = (JSONObject) obj;
                    weiboParams.clear();
                    weiboParams.put("userId",userId);
                    weiboParams.put("openId",o.getString("id"));

                    parameter = new Parameter(getService(),"queryByOpenId").setObjects(new Object[]{o.getString("id"),userId});
                    UserWeibo userWeibo = (UserWeibo) provider.execute(parameter).getModel();
                    if (userWeibo == null) {
                        userWeibo = new UserWeibo();
                        userWeibo.setUserId(userId);
                        userWeibo.setAvatar(o.getString("profile_image_url"));
                        userWeibo.setName(o.getString("name"));
                        userWeibo.setOpenId(o.getString("id"));
                        userWeibo.setSummary(o.getString("description"));
                        userWeibo.setVerified(o.getBoolean("verified"));
                        String gender = o.getString("gender");
                        if (StringUtils.equals("m",gender)) {
                            userWeibo.setGender("1");
                        } else if (StringUtils.equals("f",gender)) {
                            userWeibo.setGender("0");
                        } else {
                            userWeibo.setGender("1");
                        }

                        parameter = new Parameter("userThirdpartyService","queryByThirdParty").setObjects(new Object[] {userWeibo.getOpenId(),"SINA"});
                        UserThirdparty userThirdparty = (UserThirdparty) provider.execute(parameter).getModel();
                        if (userThirdparty != null) {
                            userWeibo.setThirdpartyId(userThirdparty.getId());
                            userWeibo.setThirdpartyUserId(userThirdparty.getUserId());
                        }

                        parameter = new Parameter(getService(),"update").setModel(userWeibo);
                        provider.execute(parameter);
                    } else {
                        parameter = new Parameter("userThirdpartyService","queryByThirdParty").setObjects(new Object[] {userWeibo.getOpenId(),"SINA"});
                        UserThirdparty userThirdparty = (UserThirdparty) provider.execute(parameter).getModel();
                        if (userThirdparty != null) {
                            userWeibo.setThirdpartyId(userThirdparty.getId());
                            userWeibo.setThirdpartyUserId(userThirdparty.getUserId());
                            parameter = new Parameter(getService(),"update").setModel(userWeibo);
                            provider.execute(parameter);
                        }
                    }
                }
                if (nextCursor == 0) {
                    flag = false;
                    params = "?access_token=" + thirdparty.getToken() + "&uid=" + thirdparty.getOpenId() + "&count=" + wbPageNum + "&cursor=" + nextCursor;
                    res = HttpUtil.httpClientPost(url + params);
                    json = JSONObject.parseObject(res);
                }
            }
        }
    }


    private void sendWeiboInvite(final WeiboInvite record) {
        executorService.submit(new Runnable() {
            public void run() {
                StringBuffer content = new StringBuffer();
                if (record.getInvites() != null && record.getInvites().size() > 0) {
                    for (UserWeibo invite : record.getInvites()) {
                        invite.setIsInvite(false);
                        Parameter parameter = new Parameter(getService(), "update").setModel(invite);
                        provider.execute(parameter);
                        content.append("@").append(invite.getName()).append(" ");
                    }
                    content.append("我最近在玩#表情说说#，最火的表情社区！一起来玩吧~下载地址:http://www.biaoqing.com");
                    try {
                        WeiboHelper.sendWeibo(record.getToken(), content.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
