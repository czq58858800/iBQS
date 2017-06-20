package com.bq.shuo.support;

import com.bq.core.util.InstanceUtil;
import com.bq.shuo.model.Album;
import com.bq.shuo.model.AlbumLiked;
import com.bq.shuo.model.Layer;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/16.
 */
public final class AlbumHelper {

    public static List<Map<String,Object>> formatLikedResultList(List records) {
        List<Map<String,Object>> resultList = InstanceUtil.newArrayList();
        for (Object obj: records) {
            AlbumLiked record = (AlbumLiked) obj;
            resultList.add(formatLikedResultMap(record));
        }
        return resultList;
    }

    public static Map<String,Object> formatLikedResultMap(AlbumLiked record) {
        Map<String, Object> resultMap = InstanceUtil.newHashMap();
        if (record != null && StringUtils.isNotBlank(record.getId())) {
            //        JSONObject exif = cover.contains("?") ? JSONObject.fromObject(cover.substring(cover.indexOf("?")+6)) : null;
            resultMap.put("uid", record.getId());
            resultMap.put("likedNum", record.getAlbum().getLikedNum());
            resultMap.put("subjectId", record.getAlbum().getSubjectId());
            resultMap.put("albumId", record.getAlbum().getId());
            resultMap.put("image", record.getAlbum().getImage());
            resultMap.put("type", record.getAlbum().getImageType());
            resultMap.put("width", record.getAlbum().getImageWidth());
            resultMap.put("height", record.getAlbum().getImageHeight());
            resultMap.put("height", record.getAlbum().getImageHeight());
            resultMap.put("createTime", record.getAlbum().getCreateTime().getTime());
            resultMap.put("enable",record.getSubject() == null ? false : record.getSubject().getEnable());
            resultMap.put("subject",SubjectHelper.formatBriefResultMap(record.getSubject()));
        }
        return resultMap;
    }

    public static Map<String,Object> formatResultMap(Album record) {
        Map<String, Object> resultMap = InstanceUtil.newHashMap();
        resultMap.put("uid", record.getId());
        resultMap.put("image", record.getImage());
        Map<String, Object> exifMap = InstanceUtil.newHashMap();
        exifMap.put("type", record.getImageType());
        exifMap.put("width", record.getImageWidth());
        exifMap.put("height", record.getImageHeight());
        resultMap.put("exif", exifMap);
        resultMap.put("isLayer", StringUtils.isNotBlank(record.getLayerId()));
        if (StringUtils.isNotBlank(record.getLayerId())) {
            Map<String, Object> layerMap = InstanceUtil.newHashMap();
            Layer layer = record.getLayer();
            layerMap.put("uid", layer.getId());
            layerMap.put("layer", layer.getLayer());
            resultMap.put("layerInfo", layerMap);
        }
        return resultMap;
    }
}
