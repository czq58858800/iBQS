package com.bq.shuo.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.bq.core.Constants;
import com.bq.core.util.CacheUtil;
import com.bq.shuo.core.helper.CounterHelper;
import com.bq.shuo.core.util.SystemConfigUtil;
import com.bq.shuo.model.Category;
import com.bq.shuo.model.Material;
import com.bq.shuo.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 素材表  服务实现类
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@Service
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"material")
public class MaterialService extends BaseService<Material> {

    @Autowired
    private CategoryService categoryService;


    public int selectCountByUserId(String userId) {
        Material record = new Material();
        record.setUserId(userId);
        record.setEnable(true);
        Wrapper<Material> wrapper = new EntityWrapper<>(record);
        return mapper.selectCount(wrapper);
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