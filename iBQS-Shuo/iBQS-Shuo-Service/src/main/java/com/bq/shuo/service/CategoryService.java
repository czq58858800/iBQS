package com.bq.shuo.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.Constants;
import com.bq.core.util.CacheUtil;
import com.bq.shuo.core.helper.CounterHelper;
import com.bq.shuo.mapper.CategoryMapper;
import com.bq.shuo.model.Category;
import com.bq.shuo.core.base.BaseService;
import com.bq.shuo.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 分类表  服务实现类
 * </p>
 *
 * @author chern.zq
 * @since 2017-04-13
 */
@Service
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"category")
public class CategoryService extends BaseService<Category> {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryCollectionService categoryCollectionService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserFollowingService userFollowingService;

    /**
     * 获取分类列表
     * @param params
     * @return 分类列表
     */
    public Page<Category> queryBeans(Map<String, Object> params) {
        Page<Category> pageInfo = query(params);
        for (Category record:pageInfo.getRecords()) {
            // 分类作者
            User author = userService.queryById(record.getUserId());
//            if (params.containsKey("myList") && record.getStuffCount() != 0) {
//                params.clear();
//                params.put("categoryId",record.getId());
//                record.setStuffs(materialProvider.query(params).getRecords());
//            }
            if (params.containsKey("currUserId")) {
                String currUserId = (String) params.get("currUserId");
                // 当前用户是否收藏了该分类
                record.setColl(categoryCollectionService.selectByIsCollId(record.getId(),currUserId));
                // 当前用户是否关注了该分类作者
                author.setFollow(userFollowingService.selectByIsFollow(currUserId,record.getUserId()));
            }
            record.setAuthor(author);
        }
        return pageInfo;
    }

    public Page<Category> queryUser(Map<String, Object> params) {
        Page<String> page = this.getPage(params);
        page.setSearchCount(false);
        page.setTotal(categoryMapper.selectCountByUserMap(params));
        page.setRecords(categoryMapper.selectIdByUserMap(page, params));
        Page<Category> pageInfo = getPage(page);
        for (Category record:pageInfo.getRecords()) {
            User author = userService.queryById(record.getUserId());
//            if (params.containsKey("myList") && record.getStuffCount() != 0) {
//                params.clear();
//                params.put("categoryId",record.getId());
//                record.setStuffs(materialProvider.query(params).getRecords());
//            }
            if (params.containsKey("currUserId")) {
                String currUserId = (String) params.get("currUserId");
                record.setColl(categoryCollectionService.selectByIsCollId(record.getId(),currUserId));
                author.setFollow(userFollowingService.selectByIsFollow(currUserId,record.getUserId()));
            }
            record.setAuthor(author);
        }
        return pageInfo;
    }

    /**
     * 获取分类详情
     * @param categoryId 分类ID
     * @param currUserId 用户ID
     * @return 分类
     */
    public Category queryBeanById(String categoryId, String currUserId) {
        Category record = queryById(categoryId);
        if (record != null) {
            // 分类详情
            return getBeanInfo(record,currUserId);
        }
        return null;
    }

    /**
     * 获取分类数量
     * @param userId 用户ID
     * @return 数量
     */
    public int selectCountByUserId(String userId) {
        Category record = new Category();
        record.setUserId(userId);
        Wrapper<Category> wrapper = new EntityWrapper<>(record);
        return mapper.selectCount(wrapper);
    }

    /**
     * 获取分类数量
     * @param categoryId 用户ID
     * @param field 字段:浏览数，收藏数
     * @return 数量
     */
    public Integer selectCategoryCounter(String categoryId,String field) {
        String key =  CounterHelper.Category.CATEGORY_COUNTER_KEY+ categoryId;

        if (CacheUtil.getCache().exists(key)) {
            String counter = (String) CacheUtil.getCache().hget(key,field);
            return Integer.parseInt(counter);
        }
        int counter = 0;
        if(StringUtils.equals(CounterHelper.Category.VIEW,field)) {
            counter = categoryMapper.selectCategoryCounter(categoryId,field);
        } else if(StringUtils.equals(CounterHelper.Category.COLLECTION,field)) {
            counter = categoryCollectionService.selectCountByCategoryId(categoryId);
        }
        CacheUtil.getCache().hset(key,field,String.valueOf(counter));
        return counter;
    }

    private Category getBeanInfo(Category record, String currUserId) {
        record.setCollNum(selectCategoryCounter(record.getId(),CounterHelper.Category.COLLECTION));
        // 判断当前用户是否收藏了该分类
        if (currUserId != null) {
            boolean isLiked = categoryCollectionService.selectByIsCollId(record.getId(),currUserId);
            record.setColl(isLiked);
        }
        // 获取用户信息
        if (record.getUserId() != null) {
            User user = userService.queryById(record.getUserId());
            if (user != null) {
                user.setFollow(userFollowingService.selectByIsFollow(currUserId,record.getUserId()));
                record.setAuthor(user);
            }
        }
        return record;
    }

    public void incrCategoryCounter(String categoryId, String field) {
        setCategoryCounter(categoryId,field,+1);
    }

    public void decrCategoryCounter(String categoryId, String field) {
        setCategoryCounter(categoryId,field,-1);
    }

    public void setCategoryCounter(String categoryId, String field,int cal) {
        if (StringUtils.isNotBlank(categoryId)) {
            String key = CounterHelper.Category.CATEGORY_COUNTER_KEY + categoryId;
            String lockKey = new StringBuilder(Constants.CACHE_NAMESPACE).append(CounterHelper.Category.CATEGORY_COUNTER_KEY).append("LOCK:").append(categoryId).toString();

            while (!CacheUtil.getLock(lockKey)) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    logger.error("", e);
                }
            }

            try {
                Integer number = selectCategoryCounter(categoryId, field);
                logger.info("素材分类（Category）"+field+":" + number+cal);
                CacheUtil.getCache().hset(key,field,String.valueOf(number+cal));
            } finally {
                CacheUtil.unlock(lockKey);
            }
        }
    }
}