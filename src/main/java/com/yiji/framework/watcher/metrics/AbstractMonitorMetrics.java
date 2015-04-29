/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-04-29 11:15 创建
 *
 */
package com.yiji.framework.watcher.metrics;

import com.yiji.framework.watcher.MonitorMetrics;

/**
 * @author qiubo@yiji.com
 */
public abstract class AbstractMonitorMetrics implements MonitorMetrics {
	
	@Override
	public int compareTo(MonitorMetrics o) {
		return this.name().compareTo(o.name());
	}
}
