package com.bq.web.shuo;

import com.bq.core.listener.SessionListener;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.model.Session;
import com.bq.shuo.provider.IShuoProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户会话管理
 * 
 * @author Harvey.Wei
 * @version 2016年5月20日 下午3:13:06
 */
@RestController
@Api(value = "会话管理", description = "会话管理")
@RequestMapping(value = "/shuo/session")
public class SessionController extends AbstractController<IShuoProvider> {
	public String getService() {
		return "sessionService";
	}

	// 查询会话
	@ApiOperation(value = "查询会话")
	@PutMapping(value = "/read/list")
	@RequiresPermissions("shuo.session.read")
	public Object get(ModelMap modelMap, @RequestBody Map<String, Object> param) {
		Integer number = SessionListener.getAllUserNumber();
		modelMap.put("userNumber", number); // 用户数大于会话数,有用户没有登录
		return super.query(modelMap, param);
	}

	@DeleteMapping
	@ApiOperation(value = "删除会话")
	@RequiresPermissions("shuo.session.delete")
	public Object delete(ModelMap modelMap, @RequestBody Session param) {
		return super.delete(modelMap, param);
	}
}
