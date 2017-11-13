package com.bq.shuo.web;

import com.alibaba.fastjson.JSONArray;
import com.bq.core.support.Assert;
import com.bq.core.support.HttpCode;
import com.bq.core.util.WebUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.model.Album;
import com.bq.shuo.model.AlbumLiked;
import com.bq.shuo.model.Subject;
import com.bq.shuo.provider.IShuoProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 专辑接口
 *
 * @author chern.zq
 * @version 2016年4月2日 下午4:20:10
 */
@RestController
@Api(value = "专辑接口", description = "专辑接口")
@RequestMapping(value = "/album")
public class AlbumController extends AbstractController<IShuoProvider> {
    @Override
    public String getService() {
        return "albumService";
    }

    /**
     * 喜欢专辑表情
     * @param request
     * @param modelMap
     * @param albumId 专辑ID，喜欢：Y:喜欢;C:取消喜欢
     * @return
     */
    @ApiOperation(value = "喜欢专辑表情")
    @PostMapping(value = "/liked")
    public Object liked(HttpServletRequest request, ModelMap modelMap,
                        @ApiParam(value = "albumId")@RequestParam(value = "albumId") String albumId,
                        @ApiParam(value = "喜欢：Y;取消:C")@RequestParam(value = "liked") String liked) {
        Assert.notNull(albumId, "ALBUM_ID");
        Assert.notNull(liked, "LIKED");
        Map<String, Object> params = WebUtil.getParameterMap(request);
        params.put("currUserId",getCurrUser());

        if (StringUtils.equals(liked.toUpperCase().trim(),"Y")) {
            Parameter updateLikedParam = new Parameter("albumLikedService","updateLiked").setObjects(new Object[]{albumId,getCurrUser()});
            if (!(Boolean) provider.execute(updateLikedParam).getObject()) {
                return setModelMap(modelMap, HttpCode.HAS_LIKED);
            }
        } else if (StringUtils.equals(liked.toUpperCase().trim(),"C")){
            Parameter updateLikedParam = new Parameter("albumLikedService","updateCancelLiked").setObjects(new Object[]{albumId,getCurrUser()});
            if (!(Boolean) provider.execute(updateLikedParam).getObject()) {
                return setModelMap(modelMap,HttpCode.NOT_LIKED);
            }
        } else {
            return setModelMap(modelMap,HttpCode.UNKNOWN_TYPE);
        }
        return setSuccessModelMap(modelMap);
    }

    /**
     * 删除喜欢专辑表情
     * @param request
     * @param modelMap
     * @return
     */
    @ApiOperation(value = "删除喜欢专辑表情")
    @PostMapping(value = "/delLiked")
    public Object delLiked(HttpServletRequest request, ModelMap modelMap,
                        @ApiParam(value = "专辑喜欢ID")@RequestParam(value = "id") String id) {
        Assert.notNull(id, "ALBUM_ID");
        Parameter parameter = new Parameter("albumLikedService","queryById").setId(id);
        AlbumLiked record = (AlbumLiked) provider.execute(parameter).getModel();

        if (record != null) {

            parameter = new Parameter("albumService","queryById").setId(record.getAlbumId());
            Album album = (Album) provider.execute(parameter).getModel();

            parameter = new Parameter("subjectService","queryById").setId(album.getSubjectId());
            Subject subject = (Subject) provider.execute(parameter).getModel();

            if (subject != null && subject.getEnable()) {
                parameter = new Parameter("albumLikedService","updateCancelLiked").setObjects(new Object[]{id,getCurrUser()});
                provider.execute(parameter);
            }

            parameter = new Parameter("albumLikedService","delete").setId(id);
            provider.execute(parameter);
        }
        return setSuccessModelMap(modelMap);
    }

    /**
     * 修改专辑排序
     * @param request
     * @param modelMap
     * @return
     */
    @ApiOperation(value = "修改专辑排序")
    @PostMapping(value = "/updateSortNo")
    public Object updateSortNo(HttpServletRequest request, ModelMap modelMap,
                               @ApiParam(required = true, value = "JSON data[] 专辑喜欢ID")@RequestParam(value = "data") String data) {
        Assert.notNull(data, "DATA");
        JSONArray dataArr = JSONArray.parseArray(data);
        Parameter selectCountByUserIdParam = new Parameter("albumLikedService","selectCountByUserId").setId(getCurrUser());
        int maxLikedCount = (int) provider.execute(selectCountByUserIdParam).getObject();
        for (int i = 0;dataArr.size() > i;i++) {
            String likedId = (String) dataArr.get(i);
            Parameter queryByIdParam = new Parameter("albumLikedService","queryById").setId(likedId);
            AlbumLiked record = (AlbumLiked) provider.execute(queryByIdParam).getModel();
            record.setSortNo(maxLikedCount-i);
            provider.execute(new Parameter("albumLikedService","update").setModel(record));
        }
        return setSuccessModelMap(modelMap);
    }
}
