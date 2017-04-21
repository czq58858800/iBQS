package com.bq.web;

import java.util.Map;

import com.bq.model.SysEmailTemplate;
import com.bq.provider.ISysProvider;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.bq.core.base.AbstractController;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 邮件模版管理控制类
 * 
 * @author Harvey.Wei
 * @version 2016年5月20日 下午3:13:31
 */
@RestController
@Api(value = "邮件模版管理", description = "邮件模版管理")
@RequestMapping(value = "emailTemplate")
public class SysEmailTemplateController extends AbstractController<ISysProvider> {
	public String getService() {
		return "sysEmailTemplateService";
	}

	@ApiOperation(value = "查询邮件模版")
	@RequiresPermissions("sys.email.template.read")
	@RequestMapping(value = "/read/list", method = RequestMethod.PUT)
	public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
		return super.query(modelMap, param);
	}

	@ApiOperation(value = "邮件模版详情")
	@RequiresPermissions("sys.email.template.read")
	@RequestMapping(value = "/read/detail", method = RequestMethod.PUT)
	public Object get(ModelMap modelMap, @RequestBody SysEmailTemplate param) {
		return super.get(modelMap, param);
	}

	@ApiOperation(value = "修改邮件模版")
	@RequiresPermissions("sys.email.template.update")
	@RequestMapping(method = RequestMethod.POST)
	public Object update(ModelMap modelMap, @RequestBody SysEmailTemplate param) {
		return super.update(modelMap, param);
	}

	@ApiOperation(value = "删除邮件模版")
	@RequiresPermissions("sys.email.template.delete")
	@RequestMapping(method = RequestMethod.DELETE)
	public Object delete(ModelMap modelMap, @RequestBody SysEmailTemplate param) {
		return super.delete(modelMap, param);
	}
}
