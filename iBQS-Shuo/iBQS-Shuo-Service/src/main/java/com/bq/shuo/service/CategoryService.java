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
 * @author Harvey.Wei
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

    public Page<Category> queryBeans(Map<String, Object> params) {
        Page<Category> pageInfo = query(params);
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


    public Category queryBeanById(String categoryId, String currUserId) {
        Category record = queryById(categoryId);

        return getBeanInfo(record,currUserId);
    }

    public int selectCountByUserId(String userId) {
        Category record = new Category();
        record.setUserId(userId);
        Wrapper<Category> wrapper = new EntityWrapper<>(record);
        return mapper.selectCount(wrapper);
    }

    public Integer selectCategoryCounter(String subjectId,String field) {
        String key =  CounterHelper.Category.CATEGORY_COUNTER_KEY+ subjectId;

        if (CacheUtil.getCache().exists(key)) {
            String counter = (String) CacheUtil.getCache().hget(key,field);
            return Integer.parseInt(counter);
        }
        int counter = 0;
        if(StringUtils.equals(CounterHelper.Category.VIEW,field)) {
            counter = categoryMapper.selectCategoryCounter(subjectId,field);
        } else if(StringUtils.equals(CounterHelper.Category.COLLECTION,field)) {
            counter = categoryCollectionService.selectCountByCategoryId(subjectId);
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