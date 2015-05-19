/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-28 15:15 创建
 *
 */
package com.yiji.framework.watcher.health;

import java.util.Set;

import com.codahale.metrics.health.HealthCheck;

/**
 * 线程死锁健康检查
 * @author qiubo@yiji.com
 */
public class ThreadDeadlockHealthCheck extends HealthCheck {
	private final ThreadDeadlockDetector detector;
	
	public ThreadDeadlockHealthCheck() {
		this(new ThreadDeadlockDetector());
	}
	
	public ThreadDeadlockHealthCheck(ThreadDeadlockDetector detector) {
		this.detector = detector;
	}
	
	@Override
	protected Result check() throws Exception {
		final Set<String> threads = detector.getDeadlockedThreads();
		if (threads.isEmpty()) {
			return Result.healthy();
		}
		return Result.unhealthy(threads.toString());
	}
	
}