/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-18 01:23 创建
 *
 */
package com.yiji.framework.watcher.metrics;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Joiner;
import com.yiji.framework.watcher.MonitorMetricsOperationException;

/**
 * @author qiubo@yiji.com
 */
public abstract class AbstractResultConvertedMonitorMetrics extends AbstractMonitorMetrics {
	public final Object monitor(Map<String, Object> params) {
		try {
			Object result = doResultConvertedMonitor(params);
			if (isResponseText(params)) {
				if (result == null) {
					return NULL;
				} else if (result instanceof Collection) {
					Collection collection = (Collection) result;
					if (collection.isEmpty()) {
						return NULL;
					} else {
						return Joiner.on("\n").join(collection);
					}
				} else if (result instanceof Map) {
					Map map = (Map) result;
					if (map.isEmpty()) {
						return NULL;
					} else {
						return Joiner.on("\n").withKeyValueSeparator("=").join(map);
					}
				} else {
					return result.toString();
				}
			}
			return result;
		} catch (Throwable e) {
			throw new MonitorMetricsOperationException(e);
		}
	}
	
	public abstract Object doResultConvertedMonitor(Map<String, Object> params) throws Throwable;
	
}
