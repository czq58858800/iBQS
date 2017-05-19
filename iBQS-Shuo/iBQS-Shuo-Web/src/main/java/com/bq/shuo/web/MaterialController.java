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
        params.put("stuffNumLT",true);
        params.put("enable",true);
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
        params.put("pageSize",20);
        params.put("enable",true);

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
            resultMap.put("isCover",record.getIsCover());
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
        if (StringUtils.isBlank(id)) {
            record.setStuffNum(0);
            record.setIsHot(false);
            record.setCitations(0);
            record.setUserId(getCurrUser());
            int sortNum = (int) provider.execute(new Parameter("categoryService","selectCountByUserId").setId(getCurrUser())).getObject();
            record.setSortNum(sortNum);
        }
        provider.execute(new Parameter("categoryService","update").setModel(record));
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
        Category category = (Category) provider.execute(new Parameter("categoryService","queryById").setId(categoryId)).getModel();
        if (category.getStuffNum() >= 20) {
            return setModelMap(modelMap,HttpCode.LIMIT_OF_STICKER);
        }

        JSONArray stuffImgJson = JSONArray.parseArray(stuffImg);
        for (int i = 0; i < stuffImgJson.size(); i++) {
            String image = (String) stuffImgJson.get(i);
            Material record = new Material();
            JSONObject imageInfo = QiniuUtil.getImageInfo(image);
            if (imageInfo.containsKey("format")) {
                record.setImageType(imageInfo.getString("format"));
                record.setImageWidth(imageInfo.getInteger("width"));
                record.setImageHeight(imageInfo.getInteger("height"));
            }
            if (i == 0 && category.getStuffNum() == 0) {
                record.setIsCover(true);
                category.setCitations(stuffImgJson.size());
                if (StringUtils.isBlank(category.getCover())) {
                    category.setStuffNum(category.getStuffNum()+stuffImgJson.size());
                    category.setCover((String) stuffImgJson.get(0));
                    category.setCoverType(record.getImageType());
                    category.setCoverWidth(record.getImageWidth());
                    category.setCoverHeight(record.getImageHeight());
                }
                provider.execute(new Parameter("categoryService","update").setModel(category));
            }
            record.setCategoryId(categoryId);
            record.setCitations(0);
            record.setImage(image);
            record.setUserId(getCurrUser());
            provider.execute(new Parameter("materialService","update").setModel(record));
        }


        return setSuccessModelMap(modelMap);
    }

    @ApiOperation(value = "修改素材")
    @PostMapping("/update")
    public Object update(HttpServletRequest request, ModelMap modelMap,
                      @ApiParam(required = true, value = "贴纸ID") @RequestParam(value = "id") String id,
                      @ApiParam(required = false, value = "设为封面") @RequestParam(value = "isCover",required = false) Boolean isCover,
                      @ApiParam(required = true, value = "贴纸名称") @RequestParam(value = "name",required = false) String name) {
        Assert.notNull(id, "ID");
        Parameter parameter = new Parameter("materialService","queryById").setId(id);
        Material material = (Material) provider.execute(parameter).getModel();
        if (!StringUtils.equals(material.getUserId(),getCurrUser())) {
            return setModelMap(modelMap,HttpCode.UNAUTHORIZED);
        }
        if (StringUtils.isNotBlank(name)) {
            material.setName(name);
        }
        if (isCover != null) {
            material.setIsCover(isCover);
        }
        provider.execute(new Parameter("materialService","update").setModel(material));
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

    /**
     * 引用贴纸
     * @param request
     * @param modelMap
     * @param id 贴纸ID
     * @return
     */
    @ApiOperation(value = "引用贴纸")
    @PostMapping(value = "/cites")
    public Object cites(HttpServletRequest request, ModelMap modelMap,
                        @ApiParam(required = true, value = "贴纸ID")@RequestParam(value = "id") String id) {
        Assert.notNull(id, "ID");
        Parameter parameter = new Parameter(getService(),"updateCites").setId(id);
        provider.execute(parameter).setId(id);
        return setSuccessModelMap(modelMap);
    }

    /**
     * 刪除贴纸
     * @param request
     * @param modelMap
     * @param id 贴纸ID
     * @return
     */
    @ApiOperation(value = "刪除贴纸")
    @PostMapping(value = "/delete")
    public Object delete(HttpServletRequest request, ModelMap modelMap,
                        @ApiParam(required = true, value = "贴纸ID")@RequestParam(value = "id") String id) {
        Assert.notNull(id, "ID");
        Parameter parameter = new Parameter(getService(),"queryById").setId(id);
        Material record = (Material) provider.execute(parameter).setId(id).getModel();
        if (!StringUtils.equals(record.getUserId(),getCurrUser())) {
            // 当前登录用户ID与创建主题的用户ID不一致
            return setModelMap(modelMap,HttpCode.UNAUTHORIZED);
        }
        record.setEnable(false);
        provider.execute(new Parameter(getService(),"update").setModel(record));
        return setSuccessModelMap(modelMap);
    }

    /**
     * 刪除贴纸分類
     * @param request
     * @param modelMap
     * @param id 贴纸ID
     * @return
     */
    @ApiOperation(value = "刪除贴纸分類")
    @PostMapping(value = "/category/delete")
    public Object categoryDelete(HttpServletRequest request, ModelMap modelMap,
                         @ApiParam(required = true, value = "贴纸分類ID")@RequestParam(value = "id") String id) {
        Assert.notNull(id, "ID");
        Parameter parameter = new Parameter("categoryService","queryById").setId(id);
        Category record = (Category) provider.execute(parameter).setId(id).getModel();
        if (!StringUtils.equals(record.getUserId(),getCurrUser())) {
            // 当前登录用户ID与创建主题的用户ID不一致
            return setModelMap(modelMap,HttpCode.UNAUTHORIZED);
        }
        record.setEnable(false);
        provider.execute(new Parameter("categoryService","update").setModel(record));
        return setSuccessModelMap(modelMap);
    }

}
