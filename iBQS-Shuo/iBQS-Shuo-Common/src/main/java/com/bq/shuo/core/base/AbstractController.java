/**
 * 
 */
package com.bq.shuo.core.base;

import com.baomidou.mybatisplus.plugins.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 控制器基类
 * 
 * @author Harvey.Wei
 * @version 2016年5月20日 下午3:47:58
 */
public abstract class AbstractController<T extends BaseProvider> extends BaseController {
    protected final Logger logger = LogManager.getLogger(this.getClass());
    @Autowired
    protected T provider;

    public abstract String getService();

    public Object query(ModelMap modelMap, Map<String, Object> param) {
        Parameter parameter = new Parameter(getService(), "query").setMap(param);
        logger.debug("{} execute query start...", parameter.getNo());
        Page<?> list = provider.execute(parameter).getPage();
        logger.debug("{} execute query end.", parameter.getNo());
        return setSuccessModelMap(modelMap, list);
    }

    public Object queryList(ModelMap modelMap, Map<String, Object> param) {
        Parameter parameter = new Parameter(getService(), "queryList").setMap(param);
        logger.debug("{} execute queryList start...", parameter.getNo());
        List<?> list = provider.execute(parameter).getList();
        logger.debug("{} execute queryList end.", parameter.getNo());
        return setSuccessModelMap(modelMap, list);
    }

    public Object get(ModelMap modelMap, BaseModel param) {
        Parameter parameter = new Parameter(getService(), "queryById").setId(param.getId());
        logger.debug("{} execute queryById start...", parameter.getNo());
        BaseModel result = provider.execute(parameter).getModel();
        logger.debug("{} execute queryById end.", parameter.getNo());
        return setSuccessModelMap(modelMap, result);
    }

    public Object update(ModelMap modelMap, BaseModel param) {
        String userId = getCurrUser();
        if (param.getId() == null) {
            param.setCreateTime(new Date());
        }
        param.setUpdateTime(new Date());
        Parameter parameter = new Parameter(getService(), "update").setModel(param);
        logger.debug("{} execute update start...", parameter.getNo());
        provider.execute(parameter);
        logger.debug("{} execute update end.", parameter.getNo());
        return setSuccessModelMap(modelMap);
    }

    public Object delete(ModelMap modelMap, BaseModel param) {
        Parameter parameter = new Parameter(getService(), "delete").setId(param.getId());
        logger.debug("{} execute delete start...", parameter.getNo());
        provider.execute(parameter);
        logger.debug("{} execute delete end.", parameter.getNo());
        return setSuccessModelMap(modelMap);
    }
}
