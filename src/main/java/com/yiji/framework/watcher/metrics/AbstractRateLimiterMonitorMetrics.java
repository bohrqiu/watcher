/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-18 00:07 创建
 *
 */
package com.yiji.framework.watcher.metrics;

import java.util.Map;

import com.google.common.util.concurrent.RateLimiter;

/**
 * 速度限制MonitorMetrics
 * @author qiubo@yiji.com
 */
public abstract class AbstractRateLimiterMonitorMetrics extends AbstractResultConvertedMonitorMetrics {
	private volatile RateLimiter rateLimiter;
    protected Object lastResult;
	public final Object doResultConvertedMonitor(Map<String, Object> params) throws Throwable {
		return rateLimiterMonitor(params);
	}
	
	private Object rateLimiterMonitor(Map<String, Object> params) throws Throwable {
		if (rateLimiter == null) {
			rateLimiter = getRateLimiter();
		}
		if (rateLimiter == null) {
			return doRateLimiterMonitor(params);
		} else {
			if (rateLimiter.tryAcquire()) {
				lastResult = doRateLimiterMonitor(params);
				return lastResult;
			} else {
				return lastResult;
			}
		}
	}
	
	/**
	 * 获取速度限制策略，如果没有速度限制，请返回null
	 */
	public RateLimiter getRateLimiter() {
		return null;
	}
	
	public abstract Object doRateLimiterMonitor(Map<String, Object> params) throws Throwable;
	
}
