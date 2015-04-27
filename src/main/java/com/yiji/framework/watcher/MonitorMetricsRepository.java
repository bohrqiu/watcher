/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-04-25 21:04 创建
 *
 */
package com.yiji.framework.watcher;

import java.util.Set;

/**
 * @author qzhanbo@yiji.com
 */
public interface MonitorMetricsRepository {
	void addMonitorMetrics(MonitorMetrics monitorMetrics);

	/**
	 * 获取所有注册的监控指标
	 */
	Set<String> names();
}
