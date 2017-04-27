package com.bq.shuo.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.support.HttpCode;
import com.bq.core.util.InstanceUtil;
import com.bq.core.util.WebUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.model.Category;
import com.bq.shuo.model.UserContacts;
import com.bq.shuo.provider.IShuoProvider;
import com.bq.shuo.support.UserHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/1/12.
 */
@RestController
@Api(value = "用户通讯录接口", description = "用户通讯录接口")
@RequestMapping(value = "user/contacts")
public class UserContactsController extends AbstractController<IShuoProvider> {
    @Override
    public String getService() {
        return "userContactsService";
    }


    // 上传用户通讯录
    @ApiOperation(value = "上传用户通讯录")
    @PostMapping("/upload")
    public Object upload(HttpServletRequest request, ModelMap modelMap,
                 @ApiParam(required = true, value = "设备ID") @RequestParam(value = "deviceId") String deviceId,
                   @ApiParam(required = true, value = "data {phone:'',name:''}") @RequestParam(value = "data") String data) {
        JSONArray jsonData = JSONArray.parseArray(data);

        String regex = "0?(13|14|15|18)[0-9]{9}";
        Pattern pattern = Pattern.compile(regex);
        for (Object obj:jsonData) {
            JSONObject o = (JSONObject) obj;
            String phone = (String) o.get("phone");
            String name = (String) o.get("name");
            phone = phone.replace("-","");
            Matcher matcher = pattern.matcher(phone);//匹配类
            while(matcher.find()){
                UserContacts record = (UserContacts) provider.execute(new Parameter("userContactsService","selectByPhone").setObjects(new Object[] {getCurrUser(),matcher.group()})).getModel();
                if (record == null) {
                    record = new UserContacts();
                    record.setDeviceId(deviceId);
                    record.setPhone(matcher.group());
                    record.setUserId(getCurrUser());
                    record.setName(name);
                    provider.execute(new Parameter("userContactsService","update").setModel(record));
                } else {
                    record.setDeviceId(deviceId);
                    provider.execute(new Parameter("userContactsService","update").setModel(record));
                }
            }
        }
        return setSuccessModelMap(modelMap,"上传成功！");
    }

    // 上传用户通讯录
    @ApiOperation(value = "获取用户通讯录")
    @GetMapping("/list")
    public Object list(HttpServletRequest request, ModelMap modelMap,
                       @ApiParam(required = true, value = "设备ID") @RequestParam(value = "deviceId") String deviceId,
                       @ApiParam(required = true, value = "状态：(1:已关注;2:未关注;3:未注册)") @RequestParam(value = "status") String status,
                       @ApiParam(required = true, value = "分页") @RequestParam(value = "pageNum") Integer pageNum) {

        Map<String, Object> params = WebUtil.getParameterMap(request);

        params.put("userId",getCurrUser());

        Parameter queryBeansParam = new Parameter("userContactsService","queryBeans").setMap(params);
        Page page = provider.execute(queryBeansParam).getPage();

        List<Map<String,Object>> resultList = InstanceUtil.newArrayList();
        for (Object obj:page.getRecords()) {
            UserContacts record = (UserContacts) obj;
            Map<String,Object> itemMap = InstanceUtil.newHashMap();


            if (StringUtils.equals(status,"1") && StringUtils.equals(status,record.getStatus())) {
                itemMap.put("phone",record.getPhone());
                itemMap.put("name",record.getName());
                itemMap.put("uid",record.getId());
                itemMap.put("user", UserHelper.formatResultMap(record.getUser()));
                resultList.add(itemMap);
            } else if (StringUtils.equals(status,"2") && StringUtils.equals(status,record.getStatus())) {
                itemMap.put("uid",record.getId());
                itemMap.put("name",record.getName());
                itemMap.put("phone",record.getPhone());
                itemMap.put("user", UserHelper.formatResultMap(record.getUser()));
                resultList.add(itemMap);
            } else if (StringUtils.equals(status,"3") && StringUtils.equals(status,record.getStatus())){
                itemMap.put("phone",record.getPhone());
                itemMap.put("name",record.getName());
                itemMap.put("uid",record.getId());
                resultList.add(itemMap);
            }
        }
        page.setRecords(resultList);
        if (resultList != null && resultList.size() > 0) {
            return setSuccessModelMap(modelMap,page);
        } else {
            return setModelMap(modelMap, HttpCode.NOT_DATA);
        }
    }


}
