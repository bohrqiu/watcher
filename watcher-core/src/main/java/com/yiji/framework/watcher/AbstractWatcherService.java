/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-25 21:09 创建
 *
 */
package com.yiji.framework.watcher;

import java.util.*;

import com.google.common.collect.Maps;

/**
 * @author qiubo@yiji.com
 */
public abstract class AbstractWatcherService implements MarshalledWatcherService {
	protected Map<String, WatcherMetrics> monitorMetricsMap = Maps.newConcurrentMap();
	
	public void add(WatcherMetrics watcherMetrics) {
		Objects.requireNonNull(watcherMetrics, "指标不能为空");
		Objects.requireNonNull(watcherMetrics.name(), "指标名字不能为空");
		if (monitorMetricsMap.containsKey(watcherMetrics.name())) {
			throw new OperationException(watcherMetrics.name() + "指标已经存在!");
		}
		monitorMetricsMap.put(watcherMetrics.name(), watcherMetrics);
	}
	
	@Override
	public Set<WatcherMetrics> set() {
		return new TreeSet<>(monitorMetricsMap.values());
	}
}
