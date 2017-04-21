package com.bq.shuo.web;

import com.bq.core.support.HttpCode;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.model.Blacklist;
import com.bq.shuo.provider.IShuoProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/2/3.
 */
@RestController
@Api(value = "黑名单接口", description = "黑名单接口")
@RequestMapping(value = "blacklist")
public class BlacklistController extends AbstractController<IShuoProvider> {

    @Override
    public String getService() {
        return "blacklistService";
    }


    /**
     * 是否在黑名单
     * @param request
     * @param modelMap
     * @return
     */
    @ApiOperation(value = "是否在黑名单")
    @GetMapping(value = "/isBlacklist")
    public Object isBlacklist(HttpServletRequest request, ModelMap modelMap,
                      @ApiParam(required = true, value = "用户ID") @RequestParam(value = "userId") String userId) {
        Parameter selectIsBlacklistByUserIdParam = new Parameter(getService(),"selectIsBlacklistByUserId").setObjects(new Object[] {getCurrUser(),userId});
        boolean isBlacklist = (boolean) provider.execute(selectIsBlacklistByUserIdParam).getObject();
        return setSuccessModelMap(modelMap,isBlacklist);
    }

    /**
     * 加入黑名单
     * @param request
     * @param modelMap
     * @return
     */
    @ApiOperation(value = "加入黑名单")
    @PostMapping(value = "/add")
    public Object add(HttpServletRequest request, ModelMap modelMap,
                         @ApiParam(required = true, value = "加入黑名单用户ID") @RequestParam(value = "userId") String userId) {
        Parameter selectIsBlacklistByUserIdParam = new Parameter(getService(),"selectIsBlackListByUserId").setObjects(new Object[] {getCurrUser(),userId});
        boolean isBlacklist = (boolean) provider.execute(selectIsBlacklistByUserIdParam).getObject();
        if (isBlacklist) {
            return setModelMap(modelMap, HttpCode.ALREADY_IN_BLACKLIST);
        }
        provider.execute(new Parameter(getService(),"update").setModel(new Blacklist(getCurrUser(),userId)));
        return setSuccessModelMap(modelMap,"保存成功");
    }

    /**
     * 移出黑名单
     * @param request
     * @param modelMap
     * @return
     */
    @ApiOperation(value = "移出黑名单")
    @PostMapping(value = "/remove")
    public Object remove(HttpServletRequest request, ModelMap modelMap,
                         @ApiParam(required = true, value = "移出黑名单用户ID") @RequestParam(value = "userId") String userId) {
        Parameter selectIsBlacklistByUserIdParam = new Parameter(getService(),"selectIsBlackListByUserId").setObjects(new Object[] {getCurrUser(),userId});
        boolean isBlacklist = (boolean) provider.execute(selectIsBlacklistByUserIdParam).getObject();
        if (!isBlacklist) {
            return setModelMap(modelMap, HttpCode.NO_BLACKLIST);
        }
        Parameter selectIdByUserIdParam = new Parameter(getService(),"selectIdByUserId").setObjects(new Object[] {getCurrUser(),userId});
        String blacklistId = provider.execute(selectIdByUserIdParam).getId();
        provider.execute(new Parameter(getService(),"delete").setId(blacklistId));
        return setSuccessModelMap(modelMap,"保存成功");
    }
}
