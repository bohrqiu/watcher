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
import java.util.SortedMap;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.google.common.collect.Maps;
import com.yiji.framework.watcher.Constants;
import com.yiji.framework.watcher.MetricsHolder;
import com.yiji.framework.watcher.extension.ExtensionLoader;
import com.yiji.framework.watcher.health.HealthCheckRepository;
import com.yiji.framework.watcher.metrics.base.AbstractWatcherMetrics;

/**
 * healtch check watch metrics,it will load {@link HealthCheck} use
 * {@link ExtensionLoader}
 * @author qiubo@yiji.com
 */
public class HealthCheckMetrics extends AbstractWatcherMetrics {
	private HealthCheckRegistry healthCheckRegistry = MetricsHolder.healthCheckRegistry();
	
	public HealthCheckMetrics() {
		ExtensionLoader extensionLoader = new ExtensionLoader();
		HealthCheckRepository healthCheckRepository = new HealthCheckRepository();
		extensionLoader.load(healthCheckRepository, HealthCheck.class);
		for (HealthCheck healthCheck : healthCheckRepository.set()) {
			healthCheckRegistry.register(getHealthCheckerName(healthCheck), healthCheck);
		}
	}
	
	private String getHealthCheckerName(HealthCheck healthCheck) {
		String simpleName = healthCheck.getClass().getSimpleName();
		int idx = simpleName.lastIndexOf("HealthCheck");
		if (idx < 0) {
			return simpleName;
		} else {
			return simpleName.substring(0, idx);
		}
	}
	
	@Override
	public Object watch(Map<String, Object> params) {
		Object key = checkNotNull(params).get(Constants.KEY);
		if (key == null) {
			SortedMap<String, HealthCheck.Result> healthChecks = healthCheckRegistry.runHealthChecks();
			boolean isAllOk = true;
			for (HealthCheck.Result singleResult : healthChecks.values()) {
				if (!singleResult.isHealthy()) {
					isAllOk = false;
					break;
				}
			}
			Map<String, Object> result = Maps.newHashMap();
			result.put("healthy", isAllOk);
			result.put("healthChecks", healthChecks);
			return result;
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
