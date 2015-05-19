/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-29 11:15 创建
 *
 */
package com.yiji.framework.watcher.metrics.base;

import java.util.Map;

import com.yiji.framework.watcher.Constants;
import com.yiji.framework.watcher.WatcherMetrics;

/**
 * @author qiubo@yiji.com
 */
public abstract class AbstractWatcherMetrics implements WatcherMetrics {

    @Override
	public int compareTo(WatcherMetrics o) {
		return this.name().compareTo(o.name());
	}
	
	protected boolean isResponseText(Map<String, Object> params) {
		Constants.ResponseType responseType = (Constants.ResponseType) params.get(Constants.RES_TYPE_KEY);
		return responseType == Constants.ResponseType.TEXT;
	}
	
	protected boolean isResponseJson(Map<String, Object> params) {
		Constants.ResponseType responseType = (Constants.ResponseType) params.get(Constants.RES_TYPE_KEY);
		return responseType == Constants.ResponseType.JSON;
	}
	
}
