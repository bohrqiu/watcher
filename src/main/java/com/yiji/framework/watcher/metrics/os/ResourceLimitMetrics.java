/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-04-26 15:28 创建
 *
 */
package com.yiji.framework.watcher.metrics.os;

import com.yiji.framework.watcher.MonitorMetrics;
import com.yiji.framework.watcher.UnsupportMonitorMetricsOperationException;

import java.util.Map;


/**
 * @author qzhanbo@yiji.com
 */
public class ResourceLimitMetrics implements MonitorMetrics {
	private Map resourceLimit = null;
	

	public Object monitor(Map<String, Object> params) {
		try {
			if (resourceLimit == null) {
				resourceLimit = SigarFactory.getSigar().getResourceLimit().toMap();
			}
			return resourceLimit;
		} catch (Exception e) {
			throw new UnsupportMonitorMetricsOperationException(e);
		}
	}
	

	public String name() {
		return "resourceLimit";
	}

	public String desc() {
		return "操作系统资源限制";
	}
}
