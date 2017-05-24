package com.bq.shuo.service;

import com.bq.core.Constants;
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
        List<String> idList = albumMapper.selectIdBySubjectId(subjectId);
        List<Album> list = InstanceUtil.newArrayList();
        if (idList != null) {
            for (String id : idList) {
                Album record = queryById(id);
                boolean isLayer = StringUtils.isNotBlank(record.getLayerId());
                if(isLayer) {
                    record.setLayer(layerService.queryById(record.getLayerId()));
                }
                boolean isLiked = false;
                if (StringUtils.isNotBlank(currUserId)) {
                    isLiked = StringUtils.isNotBlank(albumLikedService.selectByLikedId(record.getId(),currUserId));
                }
                record.setLiked(isLiked);
                list.add(record);
            }
        }
        return list;
    }


}