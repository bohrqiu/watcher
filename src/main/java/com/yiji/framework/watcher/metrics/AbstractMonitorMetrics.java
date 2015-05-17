/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-29 11:15 创建
 *
 */
package com.yiji.framework.watcher.metrics;

import java.util.Map;

import com.yiji.framework.watcher.MonitorMetrics;
import com.yiji.framework.watcher.MonitorMetricsOperationException;
import com.yiji.framework.watcher.ResponseType;

/**
 * @author qiubo@yiji.com
 */
public abstract class AbstractMonitorMetrics implements MonitorMetrics {
	
	public static Object NULL = new Object() {
		
		@Override
		public String toString() {
			return "null";
		}
	};
	/**
	 * 上次访问结果
	 */
	protected Object lastResult;
	
	@Override
	public int compareTo(MonitorMetrics o) {
		return this.name().compareTo(o.name());
	}
	
	protected boolean isResponseText(Map<String, Object> params) {
		ResponseType responseType = (ResponseType) params.get(ResponseType.RESPONSE_TYPE_KEY);
		return responseType == ResponseType.TEXT;
	}
	
	protected boolean isResponseJson(Map<String, Object> params) {
		ResponseType responseType = (ResponseType) params.get(ResponseType.RESPONSE_TYPE_KEY);
		return responseType == ResponseType.JSON;
	}
	

}
