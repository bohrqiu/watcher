/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-19 17:52 创建
 *
 */
package com.yiji.framework.watcher.dubbo.health;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.store.DataStore;
import com.codahale.metrics.health.HealthCheck;

/**
 * @author qiubo@yiji.com
 */
public class DubboThreadPoolStatusHealthCheck extends HealthCheck {
	@Override
	protected Result check() throws Exception {
		DataStore dataStore = ExtensionLoader.getExtensionLoader(DataStore.class).getDefaultExtension();
		Map<String, Object> executors = dataStore.get(Constants.EXECUTOR_SERVICE_COMPONENT_KEY);
		for (Map.Entry<String, Object> entry : executors.entrySet()) {
			ExecutorService executor = (ExecutorService) entry.getValue();
			if (executor != null && executor instanceof ThreadPoolExecutor) {
				ThreadPoolExecutor tp = (ThreadPoolExecutor) executor;
				int activeCount = tp.getActiveCount();
				int maximumPoolSize = tp.getMaximumPoolSize();
				boolean ok = maximumPoolSize - activeCount > 1;
				if (ok) {
					return Result.healthy();
				} else {
					int remainingCapacity = tp.getQueue().remainingCapacity();
					ok = remainingCapacity > 1;
					if (ok) {
						return Result.healthy();
					} else {
						return Result.unhealthy("maximumPoolSize:%s,activeCount:%s,remainingCapacity:%s", maximumPoolSize, activeCount,
							remainingCapacity);
					}
				}
			}
		}
		return Result.healthy();
	}
}
