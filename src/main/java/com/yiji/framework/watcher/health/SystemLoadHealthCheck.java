/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-19 14:24 创建
 *
 */
package com.yiji.framework.watcher.health;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import com.codahale.metrics.health.HealthCheck;

/**
 * 系统负载健康检查，检查系统负载小于cpu个数
 * @author qiubo@yiji.com
 */
public class SystemLoadHealthCheck extends HealthCheck {
	@Override
	protected Result check() throws Exception {
		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
		double load = operatingSystemMXBean.getSystemLoadAverage();
		int cpu = operatingSystemMXBean.getAvailableProcessors();
		if (load < cpu) {
			return Result.healthy();
		} else {
			return Result.unhealthy("load:%s,cpu:%s", load, cpu);
		}
		
	}
}
