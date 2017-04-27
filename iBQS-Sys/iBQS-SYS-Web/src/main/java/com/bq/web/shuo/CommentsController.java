package com.bq.web.shuo;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.model.Comments;
import com.bq.shuo.provider.IShuoProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 评论管理
 *
 * @author Harvey.Wei
 * @version 2016年4月2日 下午4:20:10
 */
@RestController
@Api(value = "评论管理", description = "评论管理")
@RequestMapping(value = "/shuo/comments")
public class CommentsController extends AbstractController<IShuoProvider> {
    @Override
    public String getService() {
        return "commentsService";
    }

    @ApiOperation(value = "查询评论")
    @RequiresPermissions("shuo.comments.read")
    @PutMapping(value = "/read/list")
    public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
        Parameter parameter = new Parameter(getService(), "query").setMap(param);
        logger.info("{} execute query start...", parameter.getNo());
        Page<Comments> list = (Page<Comments>) provider.execute(parameter).getPage();
        logger.info("{} execute query end.", parameter.getNo());
        return setSuccessModelMap(modelMap, list);
    }

    @ApiOperation(value = "评论详情")
    @RequiresPermissions("shuo.comments.read")
    @PutMapping(value = "/read/detail")
    public Object get(ModelMap modelMap, @RequestBody Comments param) {
        return super.get(modelMap, param);
    }

    @PostMapping
    @ApiOperation(value = "修改评论")
    @RequiresPermissions("shuo.comments.update")
    public Object update(ModelMap modelMap, @RequestBody Comments param) {
        return super.update(modelMap, param);
    }

    @DeleteMapping
    @ApiOperation(value = "删除评论")
    @RequiresPermissions("shuo.comments.delete")
    public Object delete(ModelMap modelMap, @RequestBody Comments param) {
        return super.delete(modelMap, param);
    }
}
