/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-19 23:12 创建
 *
 */
package com.yiji.framework.watcher.model;

import com.yiji.framework.watcher.Utils;

/**
 * @author qiubo@yiji.com
 */
public class Result extends Data {
	private boolean success;
	private String message;
	private Object data;
	private static String appName = Utils.getSysName();
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public String getAppName() {
		return appName;
	}
	
	public static Result success(Request request, Object data) {
		Result result = new Result();
		result.setSuccess(true);
		result.setData(data);
		if (request != null) {
			result.setPrettyFormat(request.isPrettyFormat());
			result.setResponseType(request.getResponseType());
		}
		return result;
	}
	
	public static Result fail(Request request, String message) {
		Result result = new Result();
		result.setSuccess(false);
		result.setMessage(message);
		if (request != null) {
			result.setPrettyFormat(request.isPrettyFormat());
			result.setResponseType(request.getResponseType());
		}
		return result;
	}
}
