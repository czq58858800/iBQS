package com.bq.shuo.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.Constants;
import com.bq.shuo.core.base.BaseService;
import com.bq.shuo.mapper.AlbumLikedMapper;
import com.bq.shuo.model.Album;
import com.bq.shuo.model.AlbumLiked;
import com.bq.shuo.model.Subject;
import com.bq.shuo.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 *   专辑喜欢 服务实现类
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

    /**
     * 获取喜欢专辑列表
     * @param params 参数
     * @return
     */
    public Page<AlbumLiked> queryBeans(Map<String, Object> params) {
        Page<AlbumLiked> pageInfo = query(params);
        for (AlbumLiked record:pageInfo.getRecords()) {
            // 获取专辑
            Album album = albumService.queryById(record.getAlbumId());
            if (album != null) {
                record.setAlbum(album);
                // 获取主题
                Subject subject = subjectService.queryById(album.getSubjectId());
                if (subject != null) {
                    // 获取主题作者
                    User user = userService.queryById(subject.getUserId());
                    if (user != null)
                        subject.setUser(user);

                    record.setSubject(subject);
                }
            }
        }
        return pageInfo;
    }

    /**
     * 修改喜欢
     * @param record
     * @return
     */
    @Override
    public AlbumLiked update(AlbumLiked record) {
        if (StringUtils.isBlank(record.getId())) {
            record.setSortNo(albumLikedMapper.selectCountByUserId(record.getUserId())+1);
        }
        return super.update(record);
    }

    /**
     * 获取用户喜欢数量
     * @param userId 用户ID
     * @return 数量
     */
    public int selectCountByUserId(String userId){
        return albumLikedMapper.selectCountByUserId(userId);
    }

    /**
     * 获取专辑喜欢ID
     * @param albumId 专辑ID
     * @param userId 用户ID
     * @return 专辑喜欢ID
     */
    public String selectByLikedId(String albumId, String userId) {
        return albumLikedMapper.selectLikedById(albumId,userId);
    }

    /**
     * 判断用户是否喜欢专辑
     * @param albumId 专辑ID
     * @param userId 用户ID
     * @return boolean 是否喜欢
     */
    public boolean selectByIsLiked(String albumId, String userId) {
        String albumLikedId = selectByLikedId(albumId,userId);
        if (StringUtils.isNotBlank(albumLikedId)) {
            return true;
        }
        return false;
    }

    /**
     * 喜欢专辑
     * @param albumId 专辑ID
     * @param currUserId 用户ID
     * @return boolean
     */
    public synchronized boolean updateLiked(String albumId, String currUserId) {
        // 获取喜欢专辑ID
        String albumLikedId = selectByLikedId(albumId,currUserId);
        if (StringUtils.isBlank(albumLikedId)) {
            // 获取专辑
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

    /**
     * 取消喜欢专辑
     * @param albumId 专辑ID
     * @param currUserId 用户ID
     * @return boolean
     */
    public synchronized boolean updateCancelLiked(String albumId, String currUserId) {
        // 获取喜欢专辑ID
        String albumLikedId = selectByLikedId(albumId,currUserId);
        if (StringUtils.isNotBlank(albumLikedId)) {
            // 获取专辑
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
                // 专辑数量只有1张时同时取消喜欢主题
                if (subject.getAlbumNum() == 1) {
                    subjectLikedService.updateCancelLiked(record.getSubjectId(),currUserId);
                }

                return true;
            }
        }
        return false;
    }
}