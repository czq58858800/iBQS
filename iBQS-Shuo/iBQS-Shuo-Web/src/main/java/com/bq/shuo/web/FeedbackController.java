package com.bq.shuo.web;

import com.bq.core.util.WebUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.model.Feedback;
import com.bq.shuo.provider.IShuoProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * ReportController
 *
 * @author chern.zq
 * @date 2016/12/23 0023
 */
@RestController
@Api(value = "反馈/意见接口", description = "反馈/意见接口")
@RequestMapping(value = "feedback")
public class FeedbackController extends AbstractController<IShuoProvider> {


    @Override
    public String getService() {
        return "feedbackService";
    }


    // 提交
    @ApiOperation(value = "提交")
    @PostMapping("/sub")
    public Object sub(HttpServletRequest request, ModelMap modelMap,
                      @ApiParam(required = true,value = "设备") @RequestParam(value = "device") String device,
                      @ApiParam(required = true, value = "提交内容") @RequestParam(value = "subContent") String subContent,
                      @ApiParam(required = false, value = "联系人(联系方式)") @RequestParam(value = "contacts",required = false) String contacts) {
        Feedback record = new Feedback();
        record.setDevice(device);
        record.setIp(WebUtil.getHost(request));
        record.setSubContent(subContent);
        record.setUserId(getCurrUser());
        record.setContacts(contacts);
        Parameter parameter = new Parameter(getService(),"update").setModel(record);
        provider.execute(parameter);
        return setSuccessModelMap(modelMap);
    }
}
