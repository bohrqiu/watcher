/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-28 14:39 创建
 *
 */
package com.yiji.framework.watcher.metrics;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.yiji.framework.watcher.Constants;
import com.yiji.framework.watcher.MetricsHolder;
import com.yiji.framework.watcher.extension.ExtensionLoader;
import com.yiji.framework.watcher.health.HealthCheckRepository;
import com.yiji.framework.watcher.metrics.base.AbstractMonitorMetrics;

/**
 * healtch check monitor metrics,it will load {@link HealthCheck} use
 * {@link ExtensionLoader}
 * @author qiubo@yiji.com
 */
public class HealthCheckMetrics extends AbstractMonitorMetrics {
	private HealthCheckRegistry healthCheckRegistry = MetricsHolder.healthCheckRegistry();
	
	public HealthCheckMetrics() {
		ExtensionLoader extensionLoader = new ExtensionLoader();
		HealthCheckRepository healthCheckRepository = new HealthCheckRepository();
		extensionLoader.load(healthCheckRepository, HealthCheck.class);
		for (HealthCheck healthCheck : healthCheckRepository.set()) {
			healthCheckRegistry.register(healthCheck.getClass().getSimpleName(), healthCheck);
		}
	}
	
	@Override
	public Object monitor(Map<String, Object> params) {
		Object key = checkNotNull(params).get(Constants.KEY);
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
		return "health check. Optional parameter: key=xx";
	}
	
	public Set<String> getHealthCheckNames() {
		return healthCheckRegistry.getNames();
	}
	
}
