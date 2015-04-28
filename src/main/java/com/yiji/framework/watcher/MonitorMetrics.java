/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-04-24 23:52 创建
 *
 */
package com.yiji.framework.watcher;

import java.util.Map;

/**
 * @author qzhanbo@yiji.com
 */
public interface MonitorMetrics extends MetricsName, Comparable<MonitorMetrics> {
	/**
	 * 参数名字
	 */
	String KEY = "key";
	
	Object monitor(Map<String, Object> params);
	
	@Override
	default int compareTo(MonitorMetrics o) {
		return this.name().compareTo(o.name());
	}
}
