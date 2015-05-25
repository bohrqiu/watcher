/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-19 18:08 创建
 *
 */
package com.yiji.framework.watcher.dubbo.metrics;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.store.DataStore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiji.framework.watcher.WatcherException;

/**
 * @author qiubo@yiji.com
 */
public class DubboThreadPoolStatusMetrics extends AbstractDubboWatcherMetrics {
	
	@Override
	public Object doMonitor(Map<String, Object> params) throws Throwable {
		List<Map<String, Object>> result = Lists.newArrayList();
		DataStore dataStore = ExtensionLoader.getExtensionLoader(DataStore.class).getDefaultExtension();
		Map<String, Object> executors = dataStore.get(Constants.EXECUTOR_SERVICE_COMPONENT_KEY);
		if (executors == null || executors.isEmpty()) {
			throw WatcherException.throwIt("no executors found");
		}
		for (Map.Entry<String, Object> entry : executors.entrySet()) {
			String port = entry.getKey();
			ExecutorService executor = (ExecutorService) entry.getValue();
			if (executor != null && executor instanceof ThreadPoolExecutor) {
				ThreadPoolExecutor tp = (ThreadPoolExecutor) executor;
				Map<String, Object> threadPoolResult = Maps.newHashMap();
				threadPoolResult.put("maximumPoolSize", tp.getMaximumPoolSize());
				threadPoolResult.put("corePoolSize", tp.getCorePoolSize());
				threadPoolResult.put("largestPoolSize", tp.getLargestPoolSize());
				threadPoolResult.put("activeCount", tp.getActiveCount());
				threadPoolResult.put("queuedTask", tp.getQueue().size());
				threadPoolResult.put("queueRemainingCapacity", tp.getQueue().remainingCapacity());
				threadPoolResult.put("port", port);
				result.add(threadPoolResult);
			}
		}
		return result;
	}
	
	@Override
	public String name() {
		return "dubboThreadPoolStatus";
	}
	
	@Override
	public String desc() {
		return "dubbo thread pool status";
	}
	
	@Override
	public CacheTime getCacheTime() {
		return CacheTime.THIRTY_SECOND;
	}
}
