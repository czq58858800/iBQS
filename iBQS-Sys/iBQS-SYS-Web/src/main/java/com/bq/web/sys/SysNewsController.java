package com.bq.web.sys;

import java.util.Map;

import com.bq.model.SysNews;
import com.bq.provider.ISysProvider;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.bq.core.base.AbstractController;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 新闻管理控制类
 * 
 * @author chern.zq
 * @version 2016年5月20日 下午3:13:31
 */
@RestController
@Api(value = "新闻管理", description = "新闻管理")
@RequestMapping(value = "news")
public class SysNewsController extends AbstractController<ISysProvider> {
	public String getService() {
		return "sysNewsService";
	}

	@ApiOperation(value = "查询新闻")
	@RequiresPermissions("public.news.read")
	@PutMapping(value = "/read/list")
	public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
		return super.query(modelMap, param);
	}

	@ApiOperation(value = "新闻详情")
	@RequiresPermissions("public.news.read")
	@PutMapping(value = "/read/detail")
	public Object get(ModelMap modelMap, @RequestBody SysNews param) {
		return super.get(modelMap, param);
	}

	@PostMapping
	@ApiOperation(value = "修改新闻")
	@RequiresPermissions("public.news.update")
	public Object update(ModelMap modelMap, @RequestBody SysNews param) {
		return super.update(modelMap, param);
	}

	@DeleteMapping
	@ApiOperation(value = "删除新闻")
	@RequiresPermissions("public.news.delete")
	public Object delete(ModelMap modelMap, @RequestBody SysNews param) {
		return super.delete(modelMap, param);
	}
}
