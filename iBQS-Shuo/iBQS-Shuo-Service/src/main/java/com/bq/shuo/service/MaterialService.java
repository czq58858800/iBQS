package com.bq.shuo.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.bq.core.Constants;
import com.bq.core.util.CacheUtil;
import com.bq.core.util.InstanceUtil;
import com.bq.shuo.core.base.BaseService;
import com.bq.shuo.core.util.SystemConfigUtil;
import com.bq.shuo.mapper.MaterialMapper;
import com.bq.shuo.model.Category;
import com.bq.shuo.model.Material;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 素材表  服务实现类
 * </p>
 *
 * @author chern.zq
 * @since 2017-04-13
 */
@Service
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"material")
public class MaterialService extends BaseService<Material> {

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private CategoryService categoryService;


    public int selectCountByUserId(String userId) {
        Material record = new Material();
        record.setUserId(userId);
        record.setEnable(true);
        Wrapper<Material> wrapper = new EntityWrapper<>(record);
        return mapper.selectCount(wrapper);
    }


    public List<Material> selectAllByCategoryId(String categoryId) {
        return getList(materialMapper.selectAll(categoryId));
    }

    public int selectCountByCategory(String categoryId) {
        return materialMapper.selectCountByCategory(categoryId);
    }

    @Override
    public Material update(Material record) {
        if (StringUtils.isNotBlank(record.getId())) {

            if (record.getIsCover()) {
                Map<String, Object> params = InstanceUtil.newHashMap();
                params.put("categoryId", record.getCategoryId());
                params.put("isCover", record.getIsCover());
                List<Material> materialList = queryList(params);
                if (materialList != null && materialList.size() > 0) {
                    Material material = materialList.get(0);
                    material.setIsCover(false);
                    super.update(material);
                }

                Category category = categoryService.queryById(record.getCategoryId());

                category.setCover(record.getImage());
                category.setCoverType(record.getImageType());
                category.setCoverWidth(record.getImageWidth());
                category.setCoverHeight(record.getImageHeight());

                categoryService.update(category);
            }
        }
        return super.update(record);
    }

    public void updateCites(String id) {
        String lockKey = new StringBuilder(Constants.CACHE_NAMESPACE).append("material:updateCites:").append("LOCK:").append(id).toString();
        while (!CacheUtil.getLock(lockKey)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }
        try {
            Material record = queryById(id);
            if (record != null) {
                record.setCitations(record.getCitations()+1);
                update(record);

                Category category = categoryService.queryById(record.getCategoryId());
                if (category != null) {
                    category.setCitations(category.getCitations()+1);

                    int maxCount = SystemConfigUtil.getIntValue(SystemConfigUtil.STICKER_CITES_NUM);
                    if (category.getCitations() == maxCount) {
                        category.setHotTime(new Date());
                        category.setIsHot(true);
                    }
                    categoryService.update(category);
                }
            }
        } finally {
            CacheUtil.unlock(lockKey);
        }
    }
}