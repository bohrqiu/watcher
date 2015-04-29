/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-04-29 11:09 创建
 *
 */
package com.yiji.framework.watcher;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.health.SharedHealthCheckRegistries;

/**
 * @author qzhanbo@yiji.com
 */
public class MetricsHolder {
	private static MetricRegistry metricRegistry = SharedMetricRegistries.getOrCreate("metricRegistry");
	private static HealthCheckRegistry healthCheckRegistry = SharedHealthCheckRegistries.getOrCreate("healthCheckRegistry");
	
	public static MetricRegistry metricRegistry() {
		return metricRegistry;
	}
	
	public static HealthCheckRegistry healthCheckRegistry() {
		return healthCheckRegistry;
	}
}
