/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-19 13:58 创建
 *
 */
package com.yiji.framework.watcher.health;

import com.codahale.metrics.health.HealthCheck;

/**
 * jvm可用内存健康检查，检查可用内存是否小于 {@link MemoryStatusHealthCheck#avalibleMemthreshold}
 * @author qiubo@yiji.com
 */
public class MemoryStatusHealthCheck extends HealthCheck {
	private static final long avalibleMemthreshold = 5 * 1024 * 1024;//可用内存阈值=5M
	
	@Override
	protected Result check() throws Exception {
		Runtime runtime = Runtime.getRuntime();
		long freeMemory = runtime.freeMemory();
		long totalMemory = runtime.totalMemory();
		long maxMemory = runtime.maxMemory();
		boolean ok = maxMemory - (totalMemory - freeMemory) > avalibleMemthreshold; // 剩余空间小于2M报警
		if (ok) {
			return Result.healthy();
		} else {
			String msg = "max:" + (maxMemory / 1024 / 1024) + "M,total:" + (totalMemory / 1024 / 1024) + "M,used:"
							+ ((totalMemory / 1024 / 1024) - (freeMemory / 1024 / 1024)) + "M,free:" + (freeMemory / 1024 / 1024) + "M";
			return Result.unhealthy(msg);
		}
	}
}
