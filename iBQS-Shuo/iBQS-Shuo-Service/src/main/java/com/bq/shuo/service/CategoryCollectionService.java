package com.bq.shuo.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.bq.core.Constants;
import com.bq.core.util.InstanceUtil;
import com.bq.shuo.core.base.BaseService;
import com.bq.shuo.core.helper.CounterHelper;
import com.bq.shuo.core.util.SystemConfigUtil;
import com.bq.shuo.mapper.CategoryCollectionMapper;
import com.bq.shuo.model.Category;
import com.bq.shuo.model.CategoryCollection;
import com.bq.shuo.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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
@CacheConfig(cacheNames = Constants.CACHE_SHUO_NAMESPACE+"categoryCollection")
public class CategoryCollectionService extends BaseService<CategoryCollection> {
    @Autowired
    private CategoryCollectionMapper categoryCollectionMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserFollowingService userFollowingService;

    /**
     * 获取素材收藏列表
     * @param params 参数
     * @return 列表
     */
    public Page<CategoryCollection> queryBeans(Map<String, Object> params) {
        Page<CategoryCollection> page = super.query(params);
        String userId = (String) params.get("currUserId");
        for (CategoryCollection record:page.getRecords()) {
            // 获取分类
            record.setCategory(categoryService.queryBeanById(record.getCategoryId(),userId));
        }
        return page;
    }

    public Page<CategoryCollection> querByUsers(Map<String, Object> params) {
        Page<String> idPage = this.getPage(params);
        idPage.setRecords(categoryCollectionMapper.selectIdPage(idPage,params));
        Page<CategoryCollection> pageInfo = getPage(idPage,CategoryCollection.class);

        List<CategoryCollection> resultList = InstanceUtil.newArrayList();
        for (CategoryCollection record:pageInfo.getRecords()) {
            User user = userService.queryById(record.getUserId());
            if (params.containsKey("currUserId")) {
                String currUserId = (String) params.get("currUserId");
                user.setFollow(userFollowingService.selectByIsFollow(record.getUserId(),currUserId));
                record.setUser(user);
            }
            record.setUser(user);
            resultList.add(record);
        }
        pageInfo.setRecords(resultList);
        return pageInfo;
    }

    /**
     * 收藏素材
     * @param categoryId 分类ID
     * @param userId 用户ID
     * @return 是否收藏
     */
    public boolean updateLiked(String categoryId, String userId) {
        if (StringUtils.isBlank(selectByCollId(categoryId,userId))) {
            Category record = categoryService.queryById(categoryId);
            if (record!=null) {
                update(new CategoryCollection(userId,categoryId));
                // 主题喜欢数+1
                categoryService.incrCategoryCounter(categoryId, CounterHelper.Category.COLLECTION);
                // 作者素材分类数+1
                userService.incrUserCounter(record.getUserId(),CounterHelper.User.STUFF_COLL);
                // 用户收藏素材分类数+1
                userService.incrUserCounter(userId,CounterHelper.User.MY_COLL_STUFF);

                int collectionCount = selectMyCollCountByUserId(categoryId);
                int maxCount = SystemConfigUtil.getIntValue(SystemConfigUtil.CATEGORY_COLLECTION_HOT_NUM);
                if (collectionCount == maxCount) {
                    record.setIsHot(true);
                    record.setHotTime(new Date());
                    categoryService.update(record);
                }
                return true;
            }
        }

        return false;
    }

    /**
     * 取消收藏素材
     * @param categoryId 分类ID
     * @param userId 用户ID
     * @return 是否取消收藏
     */
    public boolean updateCancelLiked(String categoryId, String userId) {
        String followId = selectByCollId(categoryId,userId);
        if (StringUtils.isNotBlank(followId)) {
            delete(followId);
            Category record = categoryService.queryById(categoryId);
            if (record!=null && record.getEnable()) {
                // 主题喜欢数-1
                categoryService.decrCategoryCounter(categoryId,CounterHelper.Category.COLLECTION);
                // 作者作品喜欢数-1
                userService.decrUserCounter(record.getUserId(),CounterHelper.User.STUFF_COLL);
                // 用户喜欢作品数-1
                userService.decrUserCounter(userId,CounterHelper.User.MY_COLL_STUFF);
                return true;
            }
        }
        return false;
    }

    /**
     * 获取收藏ID
     * @param categoryId 分类ID
     * @param userId 用户ID
     * @return 收藏ID
     */
    public String selectByCollId(String categoryId, String userId) {
        return categoryCollectionMapper.selectCollById(categoryId,userId);
    }

    /**
     * 获取素材被收藏数量
     * @param categoryId 分类ID
     * @return 数量
     */
    public int selectCountByCategoryId(String categoryId) {
        return categoryCollectionMapper.selectCountByCategoryId(categoryId);
    }

    public boolean selectByIsCollId(String albumId, String userId) {
        String albumLikedId = selectByCollId(albumId,userId);
        if (StringUtils.isNotBlank(albumLikedId)) {
            return true;
        }
        return false;
    }

    /**
     * 获取用户收藏数量
     * @param userId 用户ID
     * @return 数量
     */
    public int selectCountByUserId(String userId) {
        return categoryCollectionMapper.selectCountByUserId(userId);
    }

    /**
     * 获取用户收藏数量
     * @param userId 用户ID
     * @return 数量
     */
    public int selectMyCollCountByUserId(String userId) {
        return categoryCollectionMapper.selectMyCollCountByUserId(userId);
    }

    @Override
    public CategoryCollection update(CategoryCollection record) {
        if (StringUtils.isBlank(record.getId())) {
            record.setSortNo(selectCountByUserId(record.getUserId())+1);
        }
        return super.update(record);
    }
}