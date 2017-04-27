package com.bq.shuo.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.Constants;
import com.bq.core.support.Assert;
import com.bq.core.support.HttpCode;
import com.bq.core.util.CacheUtil;
import com.bq.core.util.InstanceUtil;
import com.bq.core.util.WebUtil;
import com.bq.shuo.core.base.AbstractController;
import com.bq.shuo.core.base.Parameter;
import com.bq.shuo.core.util.Push;
import com.bq.shuo.core.util.PushType;
import com.bq.shuo.core.util.QiniuUtil;
import com.bq.shuo.model.*;
import com.bq.shuo.provider.IShuoProvider;
import com.bq.shuo.support.SubjectHelper;
import com.bq.shuo.support.SubjectLikedHelper;
import com.bq.shuo.support.TopicHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 表情接口
 *
 * @author Harvey.Wei
 * @version 2016年4月2日 下午4:20:10
 */
@RestController
@Api(value = "表情接口", description = "表情接口")
@RequestMapping(value = "/subject")
public class SubjectController extends AbstractController<IShuoProvider> {
    @Override
    public String getService() {
        return "subjectService";
    }

    // 主题列表
    @ApiOperation(value = "主题列表")
    @GetMapping("/list")
    public Object list(HttpServletRequest request, ModelMap modelMap,
                       @ApiParam(value = "页码") @RequestParam(value = "pageNum") Integer pageNum,
                       @ApiParam(value = "关键字{0:最新;1:最热}") @RequestParam(value = "keyword") String keyword) {
        Assert.notNull(pageNum, "PAGE_NUM");
        Assert.notNull(keyword, "KEYWORD");
        Map<String, Object> params = WebUtil.getParameterMap(request);
        params.put("currUserId",getCurrUser());
        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        List<Map<String,Object>> resultList = InstanceUtil.newArrayList();
        if (StringUtils.equals(keyword, "1") || StringUtils.equals("HOT",keyword.toUpperCase()) || StringUtils.equals("最热",keyword)) { // 热门主题
            params.remove("keyword");
            params.put("orderHot",true);

            Parameter queryByHotParam = new Parameter(getService(),"queryByHot").setMap(params);
            Page subjectHotPage = provider.execute(queryByHotParam).getPage();
            if (pageNum == 1) {  // 当页码为第一条时同时返回话题
                Parameter queryTopicByHotParam = new Parameter("topicsService","queryByHot").setMap(params);
                List topicList = provider.execute(queryTopicByHotParam).getList();
                resultMap.put("topics", TopicHelper.formatResultList(topicList));
            }
            resultMap.put("subject",SubjectHelper.formatResultList(subjectHotPage.getRecords()));
            resultList.add(resultMap);
            subjectHotPage.setRecords(resultList);
            return setSuccessModelMap(modelMap,subjectHotPage);
        } else if(StringUtils.equals(keyword, "0") || StringUtils.equals("NEW",keyword.toUpperCase())  || StringUtils.equals("最新",keyword)) {
            params.remove("keyword");
            Parameter queryByNewParam = new Parameter(getService(),"queryByNew").setMap(params);
            Page subjectNewPage = provider.execute(queryByNewParam).getPage();
            resultMap.put("subject",SubjectHelper.formatResultList(subjectNewPage.getRecords()));
            resultList.add(resultMap);
            subjectNewPage.setRecords(resultList);
            return setSuccessModelMap(modelMap,subjectNewPage);
        } else {
            Parameter queryBeansParam = new Parameter(getService(),"queryBeans").setMap(params);
            Page subjectPage = provider.execute(queryBeansParam).getPage();
            resultMap.put("subject", SubjectHelper.formatResultList(subjectPage.getRecords()));
            resultList.add(resultMap);
            subjectPage.setRecords(resultList);
            return setSuccessModelMap(modelMap,subjectPage);
        }
    }

    // 主题推荐列表
    @ApiOperation(value = "主题推荐列表")
    @GetMapping("/recomment/list")
    public Object recommentList(HttpServletRequest request,ModelMap modelMap,HttpSession session,
                                @ApiParam(required = true, value = "主题ID") @RequestParam(value = "id") String id) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        Subject subject = (Subject) provider.execute(new Parameter(getService(),"queryById").setId(id)).getModel();
        List<String> topic = SubjectHelper.findTopic(subject.getContent());
        if (topic != null && topic.size() > 0) {
            params.put("keyword", topic.get(0));
        }

        int rowLimit = (int) provider.execute(new Parameter(getService(),"selectRowByMap").setMap(params)).getObject();
        params.put("startLimit", rowLimit);
        params.put("endLimit", 8);
        params.put("currUserId", getCurrUser());


        List<Subject> subjects = (List<Subject>) provider.execute(new Parameter(getService(),"queryByRecommentNew").setMap(params)).getList();
        if (subjects.size() < 8) {
            params.remove("keyword");
            String key = new StringBuilder(Constants.CACHE_NAMESPACE).append("UserSubjectRecomment:").append(session.getId()).toString();
            int endLimit = 8-subjects.size();
            if (CacheUtil.getCache().exists(key)) {
                Integer startLimit = (Integer) CacheUtil.getCache().get(key);
                params.put("startLimit", startLimit);
                CacheUtil.getCache().set(key,endLimit+startLimit);
            } else {
                params.put("startLimit", 0);
                CacheUtil.getCache().set(key,endLimit);
            }

            params.put("endLimit", endLimit);

            subjects.addAll((List<Subject>) provider.execute(new Parameter(getService(),"queryByRecommentNew").setMap(params)).getList());
        }
        return setSuccessModelMap(modelMap, SubjectHelper.formatResultList(subjects));
    }

    // 详细信息
    @ApiOperation(value = "主题详情")
    @GetMapping(value = "/detail")
    public Object detail(ModelMap modelMap,
                         @ApiParam(required = true, value = "主题ID")@RequestParam(value = "id") String id) {
        Parameter parameter = new Parameter(getService(),"queryBeanById").setObjects(new Object[] {id,getCurrUser()});
        Subject record = (Subject) provider.execute(parameter).getModel();
        if (record != null && StringUtils.isNotBlank(record.getId()) && record.getEnable()) {
            return setSuccessModelMap(modelMap, SubjectHelper.formatResultMap(record));
        }
        return setModelMap(modelMap, HttpCode.NOT_DATA);
    }

    // 喜欢主题的用户列表
    @ApiOperation(value = "喜欢主题的用户列表")
    @GetMapping(value = "/liked/user/list")
    public Object likeds(HttpServletRequest request, ModelMap modelMap,
                         @ApiParam(required = true, value = "页码") @RequestParam(value = "pageNum") Integer pageNum,
                         @ApiParam(required = true, value = "主题ID")@RequestParam(value = "subjectId") String id) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        Parameter parameter = new Parameter("subjectLikedService","queryBeans").setMap(params);
        Page page = provider.execute(parameter).getPage();
        page.setRecords(SubjectLikedHelper.formatResultList(page.getRecords()));
        return setSuccessModelMap(modelMap, page);
    }

    // 删除个人主题
    @ApiOperation(value = "删除个人主题")
    @PostMapping(value = "/delete")
    public Object delete(HttpServletRequest request, ModelMap modelMap,
                         @ApiParam(required = true, value = "主题ID")@RequestParam(value = "id") String id) {
        Parameter parameter = new Parameter(getService(),"queryById").setId(id);
        Subject record = (Subject) provider.execute(parameter).getModel();

        if (!StringUtils.equals(record.getUserId(),getCurrUser())) {
            // 当前登录用户ID与创建主题的用户ID不一致
            return setModelMap(modelMap,HttpCode.UNAUTHORIZED);
        }
        parameter = new Parameter(getService(),"delete").setId(id);
        provider.execute(parameter);
        return setSuccessModelMap(modelMap);
    }

    /**
     * 喜欢主题
     * @param request
     * @param modelMap
     * @param id 用户ID，喜欢：Y:喜欢;C:取消喜欢
     * @return
     */
    @ApiOperation(value = "喜欢主题")
    @PostMapping(value = "/liked")
    public Object liked(HttpServletRequest request, ModelMap modelMap,
                        @ApiParam(required = true, value = "主题ID")@RequestParam(value = "id") String id,
                        @ApiParam(required = true, value = "喜欢：Y;取消:C")@RequestParam(value = "liked") String liked) {

        liked = liked.toUpperCase().trim();
        if (StringUtils.equals(liked,"Y"))  {
            Parameter parameter = new Parameter("subjectLikedService","updateLiked").setObjects(new Object[]{id,getCurrUser()});
            boolean isLiked = (boolean) provider.execute(parameter).getObject();
            if (!isLiked) {
                parameter = new Parameter("subjectService","queryBeanById").setObjects(new Object[]{id,getCurrUser()});
                Subject subject = (Subject) provider.execute(parameter).getModel();
                // 评论推送
                new Push(PushType.LIKED,getCurrUser(),subject.getUserId());
                return setModelMap(modelMap,HttpCode.HAS_LIKED);
            }
        } else if (StringUtils.equals(liked,"C")){
            Parameter parameter = new Parameter("subjectLikedService","updateCancelLiked").setObjects(new Object[]{id,getCurrUser()});
            boolean isLiked = (boolean) provider.execute(parameter).getObject();
            if(!isLiked){
                return setModelMap(modelMap,HttpCode.NOT_LIKED);
            }
        } else {
            return setModelMap(modelMap,HttpCode.UNKNOWN_TYPE);
        }
        return setSuccessModelMap(modelMap);
    }

    /**
     * 喜欢主题
     * @param request
     * @param modelMap
     * @param id 用户ID，喜欢：Y:喜欢;C:取消喜欢
     * @return
     */
    @ApiOperation(value = "删除喜欢主题")
    @PostMapping(value = "/delLiked")
    public Object delLiked(HttpServletRequest request, ModelMap modelMap,
                        @ApiParam(required = true, value = "喜欢ID")@RequestParam(value = "id") String id) {
        Parameter parameter = new Parameter("subjectLikedService","queryById").setId(id);
        SubjectLiked record = (SubjectLiked) provider.execute(parameter).getModel();

        if (record != null) {

            String subjectId = record.getSubjectId();
            parameter = new Parameter("subjectService","queryById").setId(subjectId);
            Subject subject = (Subject) provider.execute(parameter).getModel();

            if (subject.getEnable()) {
                parameter = new Parameter("subjectLikedService", "updateCancelLiked").setObjects(new Object[]{subjectId, getCurrUser()});
                provider.execute(parameter);
            }

            parameter = new Parameter("subjectLikedService","delete").setId(id);
            provider.execute(parameter);
        }
        return setSuccessModelMap(modelMap);
    }

    @ApiOperation(value = "封面图片Hash验证")
    @PostMapping(value = "/checkHash")
    public Object checkHash(HttpServletRequest request, ModelMap modelMap,
                            @ApiParam(required = true, value = "封面MD5哈希值") @RequestParam(value = "coverHash") String coverHash){
        Parameter parameter = new Parameter("subjectService","selectHashById").setId(coverHash);
        String subjectId = provider.execute(parameter).getId();
        if (StringUtils.isNotBlank(subjectId)) {
            parameter = new Parameter("subjectService","queryById").setId(subjectId);
            Subject subject = (Subject) provider.execute(parameter).getModel();
            return setModelMap(modelMap,HttpCode.SUBJECT_EXIST,SubjectHelper.formatBriefResultMap(subject));
        }
        return setSuccessModelMap(modelMap);
    }


    /**
     * 发布主题
     * @param request
     * @param modelMap
     * @return
     */
    @ApiOperation(value = "发布主题")
    @PostMapping(value = "/add")
    public Object add(HttpServletRequest request, ModelMap modelMap,
                      @RequestHeader(value = "Login-Device",required = false) String LoginDevice,
                      @ApiParam(required = true, value = "是否显示位置") @RequestParam(value = "isLocation") boolean isLocation,
                      @ApiParam(required = false, value = "物理位置") @RequestParam(value = "location",required = false) String location,
                      @ApiParam(required = false, value = "纬度") @RequestParam(value = "lat",required = false) Double lat,
                      @ApiParam(required = false, value = "经度") @RequestParam(value = "lng",required = false) Double lng,
                      @ApiParam(required = true, value = "封面MD5哈希值") @RequestParam(value = "coverHash") String coverHash,
                      @ApiParam(required = true, value = "封面") @RequestParam(value = "cover") String cover,
                      @ApiParam(required = true, value = "表情图片(List{'image':String,'isLayer':boolean,layerInfo:String})") @RequestParam(value = "images") String images,
                      @ApiParam(required = true, value = "内容") @RequestParam(value = "content") String content) {
        Parameter parameter = new Parameter("subjectService","selectHashById").setId(coverHash);
        String subjectId = provider.execute(parameter).getId();
        if (StringUtils.isNotBlank(subjectId)) {
            parameter = new Parameter("subjectService","queryById").setId((String) subjectId);
            Subject subject = (Subject) provider.execute(parameter).getModel();
            return setModelMap(modelMap,HttpCode.SUBJECT_EXIST,SubjectHelper.formatBriefResultMap(subject));
        }


        Subject subject = new Subject(getCurrUser(),content,isLocation,location);
        if (lat != null && lng != null) {
            subject.setLat(lat);
            subject.setLng(lng);
        }
        if (StringUtils.isNotBlank(LoginDevice)) {
            subject.setSource(LoginDevice);
        }
        JSONObject imageInfo = QiniuUtil.getImageInfo(cover);
        if (imageInfo.containsKey("format")) {
            subject.setCoverType(imageInfo.getString("format"));
            subject.setCoverWidth(imageInfo.getInteger("width"));
            subject.setCoverHeight(imageInfo.getInteger("height"));
        }
        subject.setCoverHash(coverHash);
        subject.setCover(cover);

        parameter = new Parameter("subjectService","update").setModel(subject);
        subject = (Subject) provider.execute(parameter).getModel();

        JSONArray imagesArr = JSONArray.parseArray(images);
        subject = upload(imagesArr,subject);

        parameter = new Parameter("subjectService","update").setModel(subject);
        provider.execute(parameter);

        Dynamic dynamic = new Dynamic();
        dynamic.setUserId(getCurrUser());
        dynamic.setType("1");
        dynamic.setValId(subject.getId());
        dynamic.setEnable(true);

        parameter = new Parameter("dynamicService","update").setModel(dynamic);
        provider.execute(parameter);

        // 发布推送
        new Push(PushType.SUBJECT,getCurrUser(),subject.getUserId(),subjectId,content);

        return setSuccessModelMap(modelMap);
    }

    public Subject upload(JSONArray imageData, Subject record) {
        for (Object image:imageData) {
            JSONObject imageObj = (JSONObject) image;
            String img = imageObj.getString("image");
            boolean isLayer = imageObj.getBoolean("isLayer");

            JSONObject imageInfo = QiniuUtil.getImageInfo(img);


            Album album = new Album();
            album.setImage(img);
            if (imageInfo.containsKey("format")) {
                album.setImageType(imageInfo.getString("format"));
                album.setImageWidth(imageInfo.getInteger("width"));
                album.setImageHeight(imageInfo.getInteger("height"));
            }
            album.setUserId(getCurrUser());
            album.setSubjectId(record.getId());

            if (isLayer) {
                Layer layer = new Layer();
                layer.setEnable(true);
                layer.setLayer(imageObj.getString("layerInfo"));
                layer.setUserId(getCurrUser());
                Parameter parameter = new Parameter("layerService","update").setModel(layer);
                layer = (Layer) provider.execute(parameter).getModel();
                album.setLayerId(layer.getId());
            }

            Parameter parameter = new Parameter("albumService","update").setModel(album);
            provider.execute(parameter);
        }

        record.setAlbumNum(imageData.size());
        return record;
    }

}
