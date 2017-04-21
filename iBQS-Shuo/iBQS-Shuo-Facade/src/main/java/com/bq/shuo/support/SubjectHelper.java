package com.bq.shuo.support;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.util.InstanceUtil;
import com.bq.shuo.model.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/4/14 0014.
 */
public final class SubjectHelper {
    public static List<String> findTopic(String str) {
        List<String> topic = InstanceUtil.newArrayList();
        if (StringUtils.isNotBlank(str)) {
            String regex = "#(.*?)#";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);//匹配类
            while (matcher.find()) {
                topic.add(matcher.group(1));
            }
        }
        return topic;
    }

    public static Map<String, Object> formatResultPage(Page<Subject> pageInfo) {
        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        resultMap.put("total",pageInfo.getTotal());
        resultMap.put("size",pageInfo.getSize());
        resultMap.put("pages",pageInfo.getPages());
        resultMap.put("current",pageInfo.getCurrent());
        resultMap.put("records",formatResultList(pageInfo.getRecords()));
        return resultMap;
    }

    public static List<Map<String,Object>> formatResultList(List records) {
        List<Map<String,Object>> resultList = InstanceUtil.newArrayList();
        for (Object record: records) {
            resultList.add(formatResultMap((Subject) record));
        }
        return resultList;
    }

    public static List formatSubjectLikedResultList(List<SubjectLiked> records) {
        List<Map<String,Object>> resultList = InstanceUtil.newArrayList();
        for (SubjectLiked record: records) {
            Map<String,Object> resultMap = InstanceUtil.newHashMap();
            resultMap.put("uid",record.getId());
            resultMap.put("followTime",record.getCreateTime().getTime());
            if (record != null) {
                Map<String,Object> result = formatResultMap(record.getSubject());
                if (record.getUser() != null) {
                    result.put("user",UserHelper.formatListResultMap(record.getUser()));
                }
                resultMap.put("subject", result);

            }
            resultList.add(resultMap);
        }
        return resultList;
    }

    public static Map<String,Object> formatListResultMap(Subject record) {
        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        if (record==null) return resultMap;
        resultMap.put("uid", record.getId() );
        resultMap.put("content",record.getContent());
        resultMap.put("cover",record.getCover());
        Map<String,Object> coverExif = InstanceUtil.newHashMap();
        coverExif.put("type",record.getCoverType());
        coverExif.put("width",record.getCoverWidth());
        coverExif.put("height",record.getCoverHeight());
        resultMap.put("coverExif",coverExif);
        resultMap.put("likedNum",record.getLikedNum());
        resultMap.put("albumNum", record.getAlbumNum());
        resultMap.put("isLiked",record.isLiked());
        resultMap.put("publishTime",record.getCreateTime().getTime());
        if (record.getIsLocation()) {
            resultMap.put("location",record.getLocation());
        }
        return resultMap;
    }

    public static Map<String,Object> formatResultMap(Subject record) {
        Map<String,Object> resultMap = formatListResultMap(record);
        resultMap.put("commentsNum",record.getCommentsNum());
        resultMap.put("viewNum",record.getViewNum());
        resultMap.put("isWorks",record.isWorks());
        resultMap.put("isComment",record.isComment());
        resultMap.put("user",UserHelper.formatBriefResultMap(record.getUser()));
        if (record.getAlbums() != null) {
            List<Map<String,Object>> albumList = InstanceUtil.newArrayList();
            for (Album album : record.getAlbums()) {
                Map<String,Object> albumMap = InstanceUtil.newHashMap();
                albumMap.put("uid",album.getId());
                albumMap.put("image", album.getImage());
                albumMap.put("isLiked",album.isLiked());
                Map<String,Object> exifMap = InstanceUtil.newHashMap();
                exifMap.put("type",album.getImageType());
                exifMap.put("width",album.getImageWidth());
                exifMap.put("height",album.getImageHeight());
                albumMap.put("exif", exifMap);
                albumMap.put("isLayer", StringUtils.isNotBlank(album.getLayerId()));
                if (StringUtils.isNotBlank(album.getLayerId())) {
                    Map<String,Object> layerMap = InstanceUtil.newHashMap();
                    Layer layer = album.getLayer();
                    layerMap.put("uid",layer.getId());
                    layerMap.put("layer",layer.getLayer());
                    resultMap.put("layerInfo",layerMap);
                }
                albumList.add(albumMap);
            }
            resultMap.put("albums",albumList);
        }
        return resultMap;
    }

    public static Map<String, Object> formatBriefResultMap(Subject record) {
        Map<String,Object> resultMap = InstanceUtil.newHashMap();
        if (record==null) return resultMap;
        resultMap.put("uid",record.getId());
        resultMap.put("content",record.getContent());
        resultMap.put("cover",record.getCover());
        if (record.getUser() != null) {
            resultMap.put("user",UserHelper.formatBriefResultMap(record.getUser()));
        }
        return resultMap;
    }
}
