/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-20 00:33 创建
 *
 */
package com.yiji.framework.watcher.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.yiji.framework.watcher.Constants;

/**
 * @author qiubo@yiji.com
 */
public class Data {
	private boolean prettyFormat = true;
	private Constants.ResponseType responseType = Constants.ResponseType.JSON;
	
	@JSONField(serialize = false)
	public boolean isPrettyFormat() {
		return prettyFormat;
	}
	
	public void setPrettyFormat(boolean prettyFormat) {
		this.prettyFormat = prettyFormat;
	}
	
	@JSONField(serialize = false)
	public Constants.ResponseType getResponseType() {
		return responseType;
	}
	
	public void setResponseType(Constants.ResponseType responseType) {
		this.responseType = responseType;
	}
	
	@JSONField(serialize = false)
	public boolean responsePlaintext() {
		return this.responseType == Constants.ResponseType.TEXT;
	}
	
	@JSONField(serialize = false)
	public boolean responseJson() {
		return this.responseType == Constants.ResponseType.JSON;
	}
}
