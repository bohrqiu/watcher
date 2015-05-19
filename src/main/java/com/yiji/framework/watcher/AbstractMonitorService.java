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

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.Maps;

/**
 * @author qiubo@yiji.com
 */
public abstract class AbstractMonitorService implements MonitorService {
	protected Map<String, MonitorMetrics> monitorMetricsMap = Maps.newConcurrentMap();
	
	public void add(MonitorMetrics monitorMetrics) {
		Objects.requireNonNull(monitorMetrics, "监控指标不能为空");
		Objects.requireNonNull(monitorMetrics.name(), "监控指标名字不能为空");
		if (monitorMetricsMap.containsKey(monitorMetrics.name())) {
			throw new MonitorMetricsOperationException(monitorMetrics.name() + "监控指标已经存在!");
		}
		monitorMetricsMap.put(monitorMetrics.name(), monitorMetrics);
	}
	
	@Override
	public Set<MonitorMetrics> set() {
		return new TreeSet<>(monitorMetricsMap.values());
	}
}
