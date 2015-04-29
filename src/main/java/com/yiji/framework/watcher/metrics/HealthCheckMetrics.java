/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-04-28 14:39 创建
 *
 */
package com.yiji.framework.watcher.metrics;

import java.util.Collections;
import java.util.Map;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.yiji.framework.watcher.MetricsHolder;
import com.yiji.framework.watcher.health.ThreadDeadlockHealthCheck;

/**
 * @author qzhanbo@yiji.com
 */
public class HealthCheckMetrics extends AbstractMonitorMetrics {
	private HealthCheckRegistry healthCheckRegistry = MetricsHolder.healthCheckRegistry();
	
	public HealthCheckMetrics() {
		healthCheckRegistry.register("threadDeadlockHealthCheck", new ThreadDeadlockHealthCheck());
	}
	
	@Override
	public Object monitor(Map<String, Object> params) {
		Object key = params.get(KEY);
		if (key == null) {
			return healthCheckRegistry.runHealthChecks();
		} else {
			if (!healthCheckRegistry.getNames().contains(key.toString())) {
				return Collections.emptyMap();
			}
			return healthCheckRegistry.runHealthCheck(key.toString());
		}
	}
	
	@Override
	public String name() {
		return "healthCheck";
	}
	
	@Override
	public String desc() {
		return "健康状态检查，不带任何参数，返回所有配置，传入参数key=xx，返回指定的参数.";
	}
}
