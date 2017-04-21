package com.bq.shuo.web;

import com.bq.core.support.Assert;
import com.bq.core.util.Request2ModelUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.model.Report;
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
 * @author Harvey.Wei
 * @date 2016/12/23 0023
 */
@RestController
@Api(value = "举报接口", description = "举报接口")
@RequestMapping(value = "report")
public class ReportController extends AbstractController<IShuoProvider> {
    public String getService() {
        return "reportService";
    }

    // 提交
    @ApiOperation(value = "提交")
    @PostMapping("/sub")
    public Object sub(HttpServletRequest request, ModelMap modelMap,
                       @ApiParam(required = true, value = "(表情|评论|用户)Id") @RequestParam(value = "valId") String valId,
                       @ApiParam(required = true, value = "举报:(1:表情;2:评论;3:用户)") @RequestParam(value = "type") String type,
                       @ApiParam(required = true, value = "提交内容") @RequestParam(value = "subContent") String subContent) {
        Assert.notNull(valId, "VAL_ID");
        Assert.notNull(type, "TYPE");
        Assert.notNull(subContent, "SUB_CONTENT");

        Report record = Request2ModelUtil.covert(Report.class,request);
        record.setStatus("1");
        record.setUserId(getCurrUser());
        provider.execute(new Parameter(getService(),"update").setModel(record));
        return setSuccessModelMap(modelMap);
    }
}
