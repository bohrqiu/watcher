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
 * 监控指标注册
 * @author qiubo@yiji.com
 */
public interface MonitorMetricsRepository {
	
	/**
	 * 注册监控指标
	 * @param monitorMetrics
	 */
	void addMonitorMetrics(MonitorMetrics monitorMetrics);
	
	/**
	 * 获取所有监控指标
	 * @return
	 */
	Set<MonitorMetrics> monitorMetricses();
}
