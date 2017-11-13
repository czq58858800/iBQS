package com.bq.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bq.core.util.InstanceUtil;
import com.bq.core.base.BaseModel;
import com.bq.core.base.BaseService;
import com.bq.mapper.SysMenuMapper;
import com.bq.model.SysDic;
import com.bq.model.SysMenu;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * @author chern.zq
 * @version 2016年5月20日 下午3:19:19
 */
@Service
@CacheConfig(cacheNames = "sysMenu")
public class SysMenuService extends BaseService<SysMenu> {
	@Autowired
	private SysDicService sysDicService;

	public SysMenu queryById(String id) {
		SysMenu sysMenu = super.queryById(id);
		if (sysMenu != null) {
			if (StringUtils.isBlank(sysMenu.getParentId()) || StringUtils.equals("0",sysMenu.getParentId())) {
				SysMenu parent = super.queryById(sysMenu.getParentId());
				if (parent != null) {
					sysMenu.setParentName(parent.getMenuName());
				} else {
					sysMenu.setParentId(null);
				}
			}
		}
		return sysMenu;
	}

	public List<SysMenu> queryList(Map<String, Object> params) {
		params.put("orderBy", "parent_id,sort_no");
		List<SysMenu> pageInfo = super.queryList(params);
		Map<String, String> menuTypeMap = sysDicService.queryDicByType("MENUTYPE");
		Map<String, Integer> leafMap = InstanceUtil.newHashMap();
		List<SysMenu> resultList = InstanceUtil.newArrayList();
		Map<String, List<SysMenu>> map = InstanceUtil.newHashMap();
		for (SysMenu sysMenu : pageInfo) {
			if (sysMenu.getMenuType() != null) {
				sysMenu.setTypeName(menuTypeMap.get(sysMenu.getMenuType().toString()));
			}
			if (leafMap.get(sysMenu.getParentId()) == null) {
				leafMap.put(sysMenu.getParentId(), 0);
			}
			leafMap.put(sysMenu.getParentId(), leafMap.get(sysMenu.getParentId()) + 1);
			if (map.get(sysMenu.getParentId()) == null) {
				map.put(sysMenu.getParentId(), new ArrayList<SysMenu>());
			}
			map.get(sysMenu.getParentId()).add(sysMenu);
			if (StringUtils.isBlank(sysMenu.getParentId()) || StringUtils.equals("0",sysMenu.getParentId())) {
				resultList.add(sysMenu);
			}
		}
		for (SysMenu sysMenu : pageInfo) {
			if (leafMap.get(sysMenu.getId()) != null) {
				sysMenu.setLeaf(0);
			}
			if (map.get(sysMenu.getId()) != null) {
				resultList.addAll(resultList.indexOf(sysMenu) + 1, map.get(sysMenu.getId()));
			}
		}

		return resultList;
	}


	public List<Object> queryTreeList(Map<String, Object> params) {
		params.put("orderBy", "parent_id,sort_no");
		List<SysMenu> pageInfo = super.queryList(params);
		Map<String, String> menuTypeMap = sysDicService.queryDicByType("MENUTYPE");
		Map<String, Object> dicParam = InstanceUtil.newHashMap();
		dicParam.put("type", "CRUD");
		List<SysDic> sysDics = sysDicService.queryList(dicParam);
		Map<String, Integer> leafMap = InstanceUtil.newHashMap();
		List<Object> resultList = InstanceUtil.newArrayList();
		Map<String, List<SysMenu>> map = InstanceUtil.newHashMap();
		for (SysMenu sysMenu : pageInfo) {
			if (sysMenu.getMenuType() != null) {
				sysMenu.setTypeName(menuTypeMap.get(sysMenu.getMenuType().toString()));
			}
			if (leafMap.get(sysMenu.getParentId()) == null) {
				leafMap.put(sysMenu.getParentId(), 0);
			}
			leafMap.put(sysMenu.getParentId(), leafMap.get(sysMenu.getParentId()) + 1);
			if (map.get(sysMenu.getParentId()) == null) {
				map.put(sysMenu.getParentId(), new ArrayList<SysMenu>());
			}
			map.get(sysMenu.getParentId()).add(sysMenu);
			if (StringUtils.isBlank(sysMenu.getParentId()) || StringUtils.equals("0",sysMenu.getParentId())) {
				resultList.add(sysMenu);
			}
		}
		for (SysMenu sysMenu : pageInfo) {
			if (map.get(sysMenu.getId()) != null) {
				resultList.addAll(resultList.indexOf(sysMenu) + 1, map.get(sysMenu.getId()));
			}
		}
		for (SysMenu sysMenu : pageInfo) {
			if (leafMap.get(sysMenu.getId()) != null &&leafMap.get(sysMenu.getId()) > 0) {
				sysMenu.setLeaf(0);
			} else {
				List<Map<String, Object>> dicMaps = InstanceUtil.newArrayList();
				for (SysDic sysDic : sysDics) {
					if (!"read".equals(sysDic.getCode())) {
						Map<String, Object> dicMap = InstanceUtil.transBean2Map(sysDic);
						dicMap.put("id", "D"+ sysDic.getId());
						dicMap.put("menuName", sysDic.getCodeText());
						dicMap.put("parentId", sysMenu.getId().toString());
						dicMaps.add(dicMap);
					}
				}
				resultList.addAll(resultList.indexOf(sysMenu) + 1, dicMaps);
			}
		}
		return resultList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bq.provider.SysMenuProvider#getPermissions()
	 */
	public List<Map<String, String>> getPermissions(BaseModel model) {
		return ((SysMenuMapper) mapper).getPermissions();
	}

}
