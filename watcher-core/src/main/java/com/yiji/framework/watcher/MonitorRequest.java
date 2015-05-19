/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-24 23:53 创建
 *
 */
package com.yiji.framework.watcher;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * @author qiubo@yiji.com
 */
public class MonitorRequest {
	private String action;
	private ResponseType responseType = ResponseType.JSON;
	private Map<String, Object> params = Maps.newHashMap();
	private boolean prettyFormat = true;
	
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
	
	public MonitorRequest addParam(String key, Object value) {
		params.put(key, value);
		return this;
	}
	
	public ResponseType getResponseType() {
		return responseType;
	}
	
	public void setResponseType(ResponseType responseType) {
		this.responseType = responseType;
	}
	
	public MonitorRequest responsePlaintext() {
		this.responseType = ResponseType.TEXT;
		return this;
	}
	
	public MonitorRequest responseJson() {
		this.responseType = ResponseType.JSON;
		return this;
	}
	
	public MonitorRequest prettyFormat() {
		this.prettyFormat = true;
		return this;
	}
	
	public boolean isPrettyFormat() {
		return prettyFormat;
	}
	
	public void setPrettyFormat(boolean prettyFormat) {
		this.prettyFormat = prettyFormat;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("MonitorRequest{");
		sb.append("action='").append(action).append('\'');
		sb.append(", responseType=").append(responseType);
		sb.append(", params=").append(params);
		sb.append(", prettyFormat=").append(prettyFormat);
		sb.append('}');
		return sb.toString();
	}
	
	public enum ResponseType {
		TEXT,
		JSON;
	}
}
