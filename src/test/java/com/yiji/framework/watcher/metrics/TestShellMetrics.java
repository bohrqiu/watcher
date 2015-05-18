/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-17 23:34 创建
 *
 */
package com.yiji.framework.watcher.metrics;

import java.util.Map;

import com.google.common.util.concurrent.RateLimiter;
import com.yiji.framework.watcher.metrics.shell.AbstractShellMonitorMetrics;

/**
 * @author qiubo@yiji.com
 */
public class TestShellMetrics extends AbstractShellMonitorMetrics {
	//速度限制为每5s一次
	private RateLimiter rateLimiter;
	private String lastResult;
	
	@Override
	public Object doMonitor(Map<String, Object> params) {
		if (rateLimiter == null) {
			rateLimiter = RateLimiter.create(1d / 5d);
		}
		if (rateLimiter.tryAcquire()) {
			lastResult = shellExecutor.exeShell("test/test.sh");
			return lastResult;
		} else {
			return "请求太快，数据来自于缓存\r\n" + lastResult;
		}
	}
	
	@Override
	public String name() {
		return "testshell";
	}
	
	@Override
	public String desc() {
		return "only for test";
	}
}
