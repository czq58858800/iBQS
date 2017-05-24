package com.bq.shuo.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.Constants;
import com.bq.shuo.mapper.AlbumLikedMapper;
import com.bq.shuo.model.*;
import com.bq.shuo.core.base.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 *   服务实现类
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@Service
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"albumLiked")
public class AlbumLikedService extends BaseService<AlbumLiked> {

    @Autowired
    private AlbumLikedMapper albumLikedMapper;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private SubjectLikedService subjectLikedService;

    @Autowired
    private UserService userService;

    public Page<AlbumLiked> queryBeans(Map<String, Object> params) {
        Page<AlbumLiked> pageInfo = query(params);
        for (AlbumLiked record:pageInfo.getRecords()) {
            record.setAlbum(albumService.queryById(record.getAlbumId()));
            Subject subject = subjectService.queryById(record.getAlbum().getSubjectId());
            subject.setUser(userService.queryById(subject.getUserId()));
            record.setSubject(subject);
        }
        return pageInfo;
    }

    @Override
    public AlbumLiked update(AlbumLiked record) {
        if (StringUtils.isBlank(record.getId())) {
            record.setSortNo(albumLikedMapper.selectCountByUserId(record.getUserId())+1);
        }
        return super.update(record);
    }

    public String selectByLikedId(String albumId, String userId) {
        return albumLikedMapper.selectLikedById(albumId,userId);
    }

    public boolean selectByIsLiked(String albumId, String userId) {
        String albumLikedId = selectByLikedId(albumId,userId);
        if (StringUtils.isNotBlank(albumLikedId)) {
            return true;
        }
        return false;
    }

    public synchronized boolean updateLiked(String albumId, String currUserId) {
        String albumLikedId = selectByLikedId(albumId,currUserId);
        if (StringUtils.isBlank(albumLikedId)) {
            Album record = albumService.queryById(albumId);
            if (record != null) {
                AlbumLiked albumLiked = new AlbumLiked(albumId, currUserId);

                update(albumLiked);

                // 喜欢专辑计数器
                record.setLikedNum(record.getLikedNum() + 1);
                albumService.update(record);

                // 并喜欢主题
                subjectLikedService.updateLiked(record.getSubjectId(), currUserId);
                return true;
            }
        }
        return false;
    }

    public synchronized boolean updateCancelLiked(String albumId, String currUserId) {
        String albumLikedId = selectByLikedId(albumId,currUserId);
        if (StringUtils.isNotBlank(albumLikedId)) {
            Album record = albumService.queryById(albumId);
            if (record != null) {
                delete(albumLikedId);
                // 减去-喜欢专辑计数器
                record.setLikedNum(record.getLikedNum() - 1);
                if (record.getLikedNum() < 0) {
                    record.setLikedNum(0);
                }
                albumService.update(record);


                Subject subject = subjectService.queryById(record.getSubjectId());
                if (subject.getAlbumNum() == 1) {
                    subjectLikedService.updateCancelLiked(record.getSubjectId(),currUserId);
                }

                return true;
            }
        }
        return false;
    }
}