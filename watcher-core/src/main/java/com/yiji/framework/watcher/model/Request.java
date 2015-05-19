/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-24 23:53 创建
 *
 */
package com.yiji.framework.watcher.model;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * @author qiubo@yiji.com
 */
public class Request extends Data {
	private String action;
	private Map<String, Object> params = Maps.newHashMap();
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public Map<String, Object> getParams() {
		return params;
	}
	
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	public Request addParam(String key, Object value) {
		params.put(key, value);
		return this;
	}
}
