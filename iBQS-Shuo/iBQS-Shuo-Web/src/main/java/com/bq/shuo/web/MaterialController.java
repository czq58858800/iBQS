package com.bq.shuo.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.support.Assert;
import com.bq.core.support.HttpCode;
import com.bq.core.util.InstanceUtil;
import com.bq.core.util.Request2ModelUtil;
import com.bq.core.util.WebUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.core.util.QiniuUtil;
import com.bq.shuo.model.*;
import com.bq.shuo.provider.IShuoProvider;
import com.bq.shuo.support.MaterialHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * MaterialController
 *
 * @author Harvey.Wei
 * @date 2016/11/18 0018
 */
@RestController
@Api(value = "素材接口", description = "素材接口")
@RequestMapping(value = "material")
public class MaterialController extends AbstractController<IShuoProvider> {

    public String getService() {
        return "materialService";
    }

    @ApiOperation(value = "素材热词列表")
    @GetMapping("/hotWord/list")
    public Object hotWordList(HttpServletRequest request, ModelMap modelMap,
          @ApiParam(required = true, value = "页码") @RequestParam(value = "pageNum") Integer pageNum) {

        Assert.notNull(pageNum, "PAGE_NUM");

        Map<String, Object> params = WebUtil.getParameterMap(request);

        Parameter queryParam = new Parameter("materialHotService","query").setMap(params);
        Page page = provider.execute(queryParam).getPage();
        List<Map<String,Object>> resultList = InstanceUtil.newArrayList();
        for (Object obj:page.getRecords()) {
            MaterialHot record = (MaterialHot) obj;
            Map<String,Object> resultMap = InstanceUtil.newHashMap();
            resultMap.put("value",record.getValue());
            resultMap.put("sort",record.getSort());
            resultList.add(resultMap);
        }
        page.setRecords(resultList);
        return setSuccessModelMap(modelMap, page);
    }

    @ApiOperation(value = "分类列表")
    @GetMapping("/category/list")
    public Object categoryList(HttpServletRequest request, ModelMap modelMap,
               @ApiParam(required = false, value = "每页页数") @RequestParam(value = "pageSize",required = false) Integer pageSize,
               @ApiParam(required = true, value = "页码") @RequestParam(value = "pageNum") Integer pageNum,
              @ApiParam(required = true, value = "关键字 热门=(热门|HOT) 最新=(最新|NEW)") @RequestParam(value = "keyword") String keyword) {
        Assert.notNull(pageNum, "PAGE_NUM");
        Assert.notNull(keyword, "KEYWORD");
        Map<String, Object> params = WebUtil.getParameterMap(request);
        if (!params.containsKey("audit")) {
            params.put("audit",2);
        }
        if (StringUtils.equals(keyword.trim(),"热门") || StringUtils.equals(keyword.trim().toLowerCase(),"hot")) {
            params.put("orderHot",true);
            params.remove("keyword");
        } else if (StringUtils.equals(keyword.trim(),"最新") || StringUtils.equals(keyword.trim().toLowerCase(),"new")) {
            params.remove("keyword");
        }
        params.put("currUserId",getCurrUser());

        Parameter queryBeansParam = new Parameter("categoryService","queryBeans").setMap(params);
        Page page = provider.execute(queryBeansParam).getPage();
        page.setRecords(MaterialHelper.formatCategoryResultList(page.getRecords()));
        return setSuccessModelMap(modelMap, page);
    }


    @ApiOperation(value = "素材列表")
    @GetMapping("/list")
    public Object list(HttpServletRequest request, ModelMap modelMap,
               @ApiParam(required = true, value = "页码") @RequestParam(value = "pageNum") Integer pageNum,
               @ApiParam(required = true, value = "分类") @RequestParam(value = "categoryId") String categoryId) {
        Assert.notNull(pageNum, "PAGE_NUM");
        Assert.notNull(categoryId, "CATEGORY_ID");
        Map<String, Object> params = WebUtil.getParameterMap(request);
        params.put("audit",2);

        Parameter queryParam = new Parameter("materialService","query").setMap(params);
        Page page = provider.execute(queryParam).getPage();
        List<Map<String,Object>> resultList = InstanceUtil.newArrayList();
        for (Object obj:page.getRecords()) {
            Material record = (Material) obj;
            Map<String,Object> resultMap = InstanceUtil.newHashMap();
            resultMap.put("uid",record.getId());
            resultMap.put("image",record.getImage());
            resultMap.put("citations",record.getCitations());
            resultMap.put("type",record.getImageType());
            resultMap.put("width",record.getImageWidth());
            resultMap.put("height",record.getImageHeight());
            resultMap.put("audit",record.getAudit());
            resultList.add(resultMap);
        }
        page.setRecords(resultList);

        Category category = (Category) provider.execute(new Parameter("categoryService","queryById").setId(categoryId)).getModel();
        if (category.getViewNum() == null) {
            category.setViewNum(0);
        } else {
            category.setViewNum(category.getViewNum());
        }

        provider.execute(new Parameter("categoryService","update").setModel(category));
        return setSuccessModelMap(modelMap, page);
    }

    @ApiOperation(value = "创建素材分类/修改素材分类")
    @PostMapping("/category/create")
    public Object categoryAdd(HttpServletRequest request, ModelMap modelMap,
                       @ApiParam(required = false, value = "分类ID") @RequestParam(value = "id",required = false) String id,
                       @ApiParam(required = false, value = "分类封面") @RequestParam(value = "cover",required = false) String cover,
                       @ApiParam(required = true, value = "名称") @RequestParam(value = "name") String name,
                       @ApiParam(required = true, value = "标签(tag,tag)") @RequestParam(value = "tags") String tags,
                       @ApiParam(required = true, value = "描述") @RequestParam(value = "summary") String summary) {
        Assert.notNull(name, "NAME");
        Assert.notNull(tags, "TAGS");
        Assert.notNull(summary, "SUMMARY");
        Category record = new Category(id,cover,name,tags,summary);
        CategoryReview categoryReview = new CategoryReview(id,cover,name,tags,summary,getCurrUser());
        if (StringUtils.isBlank(id)) {
            record.setAudit("0");
            record.setStuffNum(0);
            record.setIsHot(false);
            record.setCitations(0);
            record.setUserId(getCurrUser());

            int sortNum = (int) provider.execute(new Parameter("categoryService","selectCountByUserId").setId(getCurrUser())).getObject();
            record.setSortNum(sortNum);

            provider.execute(new Parameter("categoryService","update").setModel(record));
            categoryReview.setCategoryId(record.getId());
        } else {
            Category category = (Category) provider.execute(new Parameter("categoryService","queryById").setId(id)).getModel();
            if (category.getStuffNum() != 0) {
                categoryReview.setAudit("1");
            }
        }

        provider.execute(new Parameter("categoryReviewService","update").setModel(categoryReview));

        return setSuccessModelMap(modelMap);
    }

    @ApiOperation(value = "更新分类收藏排序")
    @PostMapping("/category/coll/updateSortNo")
    public Object categoryBatchSort(HttpServletRequest request, ModelMap modelMap,
                               @ApiParam(required = true, value = "分类收藏ID[id,id,...,id]") @RequestParam(value = "ids") String ids) {
        Assert.notNull(ids, "IDS");
        JSONArray dataArr = JSONArray.parseArray(ids);
        int maxLikedCount = (int) provider.execute(new Parameter("categoryCollectionService","selectCountByUserId").setId(getCurrUser())).getObject();

        for (int i = 0;dataArr.size() > i;i++) {
            String likedId = (String) dataArr.get(i);
            CategoryCollection record = (CategoryCollection) provider.execute(new Parameter("categoryCollectionService","queryById").setId(likedId)).getModel();
            record.setSortNo(maxLikedCount-i);
            provider.execute(new Parameter("categoryCollectionService","update").setModel(record));
        }

        return setSuccessModelMap(modelMap);
    }

    @ApiOperation(value = "添加素材")
    @PostMapping("/add")
    public Object add(HttpServletRequest request, ModelMap modelMap,
                       @ApiParam(required = true, value = "分类") @RequestParam(value = "categoryId") String categoryId,
                       @ApiParam(required = true, value = "素材图片([image,image,...image])") @RequestParam(value = "stuffImg") String stuffImg) {
        Assert.notNull(categoryId, "CATEGORY_ID");
        Assert.notNull(stuffImg, "STUFF_IMG");
        JSONArray stuffImgJson = JSONArray.parseArray(stuffImg);
        for (Object obj:stuffImgJson) {
            String image = (String) obj;
            Material record = new Material();
            record.setCategoryId(categoryId);
            record.setCitations(0);
            record.setImage(image);
            record.setUserId(getCurrUser());
            JSONObject imageInfo = QiniuUtil.getImageInfo(image);
            if (imageInfo.containsKey("format")) {
                record.setImageType(imageInfo.getString("format"));
                record.setImageWidth(imageInfo.getInteger("width"));
                record.setImageHeight(imageInfo.getInteger("height"));
            }
            provider.execute(new Parameter("materialService","update").setModel(record));
        }
        Category category = (Category) provider.execute(new Parameter("categoryService","queryById").setId(categoryId)).getModel();
        category.setCitations(stuffImgJson.size());
        if (StringUtils.isBlank(category.getCover())) {
            category.setStuffNum(category.getStuffNum()+stuffImgJson.size());
            category.setCover((String) stuffImgJson.get(0));
        }
        provider.execute(new Parameter("categoryService","update").setModel(category));

        return setSuccessModelMap(modelMap);
    }

    @ApiOperation(value = "素材数量")
    @GetMapping("/count")
    public Object count(HttpServletRequest request, ModelMap modelMap,
                @ApiParam(value = "用户Id") @RequestParam(value = "userId",required = false) String userId) {
        Assert.notNull(userId, "USER_ID");
        if (StringUtils.isBlank(getCurrUser()) && StringUtils.isBlank(userId)){
            return setModelMap(modelMap, HttpCode.UNAUTHORIZED);
        }

        if (StringUtils.isBlank(userId)){
            userId = getCurrUser();
        }
        Map<String,Object> dataMap = InstanceUtil.newHashMap();
        int count = (int) provider.execute(new Parameter("materialService","selectCountByUserId").setObjects(new Object[]{userId})).getObject();
        dataMap.put("count",count);
        return setSuccessModelMap(modelMap,dataMap);
    }

    /**
     * 收藏分类
     * @param request
     * @param modelMap
     * @param id 用户ID，喜欢：Y:喜欢;C:取消喜欢
     * @return
     */
    @ApiOperation(value = "收藏分类")
    @PostMapping(value = "/collection")
    public Object liked(HttpServletRequest request, ModelMap modelMap,
                        @ApiParam(required = true, value = "分类ID")@RequestParam(value = "id") String id,
                        @ApiParam(required = true, value = "喜欢：Y;取消:C")@RequestParam(value = "liked") String liked) {
        Assert.notNull(id, "ID");
        Assert.notNull(liked, "LIKED");

        liked = liked.toUpperCase().trim();
        if (StringUtils.equals(liked,"Y")) {
            Parameter isLikedParam = new Parameter("categoryCollectionService","updateLiked").setObjects(new Object[] {id,getCurrUser()});
            boolean isLiked = (boolean) provider.execute(isLikedParam).getObject();
            if (!isLiked) {
                return setModelMap(modelMap, HttpCode.HAS_LIKED);
            }
        } else if (StringUtils.equals(liked,"C")){
            Parameter isLikedParam = new Parameter("categoryCollectionService","updateCancelLiked").setObjects(new Object[] {id,getCurrUser()});
            boolean isLiked = (boolean) provider.execute(isLikedParam).getObject();
            if (!isLiked) {
                return setModelMap(modelMap,HttpCode.NOT_LIKED);
            }
        } else {
            return setModelMap(modelMap,HttpCode.UNKNOWN_TYPE);
        }
        return setSuccessModelMap(modelMap);
    }

}
