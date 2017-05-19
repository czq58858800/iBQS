package com.bq.shuo.service;

import com.bq.core.Constants;
import com.bq.core.util.CacheUtil;
import com.bq.core.util.InstanceUtil;
import com.bq.shuo.mapper.AlbumMapper;
import com.bq.shuo.model.Album;
import com.bq.shuo.core.base.BaseService;
import com.bq.shuo.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *   服务实现类
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@Service
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"album")
public class AlbumService extends BaseService<Album> {

    @Autowired
    private AlbumMapper albumMapper;

    @Autowired
    private LayerService layerService;

    @Autowired
    private AlbumLikedService albumLikedService;


    public List<Album> querySubjectIdByList(String subjectId,String currUserId) {
        String key = Constants.CACHE_SUBJECT_ALBUM+subjectId;
        List<Album> albums = InstanceUtil.newArrayList();
        if (CacheUtil.getCache().exists(key)) {
            Set<Object> ids = CacheUtil.getCache().getAll(key);
            for (Object obj : ids) {
                albums.add(queryById((String) obj,currUserId));
            }
        } else {
            List<String> idList = albumMapper.selectIdBySubjectId(subjectId);
            for (String id : idList) {
                albums.add(queryById(id,currUserId));
            }

        }
        return albums;
    }


    public Album queryById(String albumId,String currUserId) {
        Album record = queryById(albumId);
        if(StringUtils.isNotBlank(record.getLayerId())) {
            record.setLayer(layerService.queryById(record.getLayerId()));
        }
        record.setLiked(albumLikedService.selectByIsLikedId(record.getId(),currUserId));
        return record;
    }
}