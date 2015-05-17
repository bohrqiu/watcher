/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-15 18:58 创建
 *
 */
package com.yiji.framework.watcher.metrics.shell;

import static com.yiji.framework.watcher.Utils.getParam;
import static com.yiji.framework.watcher.Utils.getPid;

import java.util.Map;

import com.google.common.util.concurrent.RateLimiter;

/**
 * @author qiubo@yiji.com
 */
public class BusyJavaThreadMetrics extends AbstractShellMonitorMetrics {
	//速度限制为每5s一次
	private RateLimiter rateLimiter;
	private String lastResult;
	
	@Override
	public Object monitor(Map<String, Object> params) {
		if (rateLimiter == null) {
			rateLimiter = RateLimiter.create(1d / 5d);
		}
		if (rateLimiter.tryAcquire()) {
			Object count = getParam(params, "count", "5");
			lastResult = shellExecutor.exeShell("show-busy-java-threads.sh", "-p " + getPid(), "-c " + count);
			return lastResult;
		} else {
			return lastResult;
		}
	}
	
	@Override
	public String name() {
		return "busyJavaThread";
	}
	
	@Override
	public String desc() {
		return "show busy java thread,Optional parameters:count=10,default count=5";
	}
}
