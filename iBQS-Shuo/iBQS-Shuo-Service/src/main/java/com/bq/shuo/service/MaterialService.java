package com.bq.shuo.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.bq.shuo.model.Material;
import com.bq.shuo.core.base.BaseService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 素材表  服务实现类
 * </p>
 *
 * @author Harvey.Wei
 * @since 2017-04-13
 */
@Service
public class MaterialService extends BaseService<Material> {

    public int selectCountByUserId(String userId) {
        Material record = new Material();
        record.setUserId(userId);
        record.setEnable(true);
        Wrapper<Material> wrapper = new EntityWrapper<>(record);
        return mapper.selectCount(wrapper);
    }
}