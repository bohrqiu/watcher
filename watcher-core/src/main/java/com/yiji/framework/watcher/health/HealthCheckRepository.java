/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-19 13:20 创建
 *
 */
package com.yiji.framework.watcher.health;

import java.util.Set;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.collect.Sets;
import com.yiji.framework.watcher.extension.ExtensionRepository;

/**
 * HealthCheck extension store Repository
 * @author qiubo@yiji.com
 */
public class HealthCheckRepository implements ExtensionRepository<HealthCheck> {
	private Set<HealthCheck> healthChecks = Sets.newHashSet();
	
	@Override
	public void add(HealthCheck healthCheck) {
		healthChecks.add(healthCheck);
	}
	
	@Override
	public Set<HealthCheck> set() {
		return healthChecks;
	}
}
