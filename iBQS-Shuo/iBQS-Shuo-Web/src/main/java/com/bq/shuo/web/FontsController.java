package com.bq.shuo.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.util.WebUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.model.Fonts;
import com.bq.shuo.provider.IShuoProvider;
import com.bq.shuo.support.FontsHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * CommonMaterialController
 *
 * @author Harvey.Wei
 * @date 2016/12/16 0016
 */
@RestController
@Api(value = "字体接口", description = "字体接口")
@RequestMapping(value = "fonts")
public class FontsController extends AbstractController<IShuoProvider> {

    @Override
    public String getService() {
        return "fontsService";
    }

    // 评论列表
    @ApiOperation(value = "字体列表")
    @GetMapping("/list")
    public Object list(HttpServletRequest request, ModelMap modelMap,
                       @ApiParam(required = true, value = "页码") @RequestParam(value = "pageNum") Integer pageNum,
                       @ApiParam(required = true, value = "语言:(cn:中文;en:英文)") @RequestParam(value = "lang") String lang) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        params.put("enable",true);
        Parameter queryParam = new Parameter(getService(),"query").setMap(params);
        Page page = provider.execute(queryParam).getPage();
        page.setRecords(FontsHelper.formatResultList(page.getRecords()));
        return setSuccessModelMap(modelMap, page);
    }

    // 评论列表
    @ApiOperation(value = "根据编号获取字体")
    @GetMapping("/get/{code}")
    public Object getCode(HttpServletRequest request, ModelMap modelMap,
                       @ApiParam(required = true, value = "编号") @PathVariable(value = "code") String code) {
        Parameter queryParam = new Parameter(getService(),"getFontByCode").setId(code);
        Fonts record = (Fonts) provider.execute(queryParam).getModel();
        return setSuccessModelMap(modelMap, FontsHelper.formatResultMap(record));
    }
}
