package com.bq.shuo.core.base;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author chern.zq
 * @since 2017年2月14日 下午3:27:17
 */
@SuppressWarnings("serial")
public class Parameter implements Serializable {
	public Parameter() {
	}

	public Parameter(String service, String method) {
		this.service = service;
		this.method = method;
	}

	public Parameter(Object result) {
		if (result instanceof String) {
			this.id = (String) result;
		} else if (result instanceof BaseModel) {
			this.model = (BaseModel) result;
		} else if (result instanceof Page) {
			this.page = (Page<?>) result;
		} else if (result instanceof Map<?, ?>) {
			this.map = (Map<?, ?>) result;
		} else if (result instanceof List<?>) {
			this.list = (List<?>) result;
		} else {
			this.object = result;
		}
	}

	private String service;
	private String method;

	private String id;
	private BaseModel model;
	private Map<?, ?> map;
	private Page<?> page;
	private List<?> list;
	private Object object;
	private Object[] objects;

    private final String no = "[" + IdWorker.getId() + "]";

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getMethod() {
		return method;
	}

	public Parameter setMethod(String method) {
		this.method = method;
		return this;
	}

	public String getId() {
		return id;
	}

	public Parameter setId(String id) {
		this.id = id;
		return this;
	}

	public BaseModel getModel() {
		return model;
	}

	public Parameter setModel(BaseModel model) {
		this.model = model;
		return this;
	}

	public Map<?, ?> getMap() {
		return map;
	}

	public Parameter setMap(Map<?, ?> map) {
		this.map = map;
		return this;
	}

	public Page<?> getPage() {
		return page;
	}

	public Parameter setPage(Page<?> page) {
		this.page = page;
		return this;
	}

	public List<?> getList() {
		return list;
	}

	public Parameter setList(List<?> list) {
		this.list = list;
		return this;
	}

	public String getNo() {
        return no;
    }

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Object[] getObjects() {
		return objects;
	}

	public Parameter setObjects(Object[] objects) {
		this.objects = objects;
		return this;
	}
}
