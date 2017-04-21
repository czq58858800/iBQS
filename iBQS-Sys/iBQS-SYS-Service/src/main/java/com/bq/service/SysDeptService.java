package com.bq.service;

import com.bq.core.base.BaseService;
import com.bq.model.SysDept;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * @author Harvey.Wei
 * @version 2016年5月20日 下午3:19:19
 */
@Service
@CacheConfig(cacheNames = "sysDept")
public class SysDeptService extends BaseService<SysDept> {
	public SysDept queryById(String id) {
		SysDept sysDept = super.queryById(id);
		if (sysDept != null) {
			if (sysDept.getParentId() != null) {
				SysDept parent = super.queryById(sysDept.getParentId());
				if (parent != null) {
					sysDept.setParentName(parent.getDeptName());
				} else {
					sysDept.setParentId(null);
				}
			}
		}
		return sysDept;
	}
}
