/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-28 18:43 创建
 *
 */
package com.yiji.framework.watcher.metrics.os;

import java.util.Map;

import org.hyperic.sigar.SigarException;

import com.yiji.framework.watcher.UnsupportMonitorMetricsOperationException;
import com.yiji.framework.watcher.metrics.AbstractMonitorMetrics;

/**
 * @author qiubo@yiji.com
 */
public abstract class AbstractOSMonitorMetrics extends AbstractMonitorMetrics {
	public Object monitor(Map<String, Object> params) {
		try {
			return doMonitor(params);
		} catch (Exception e) {
			throw new UnsupportMonitorMetricsOperationException(e);
		} catch (UnsatisfiedLinkError error) {
			throw new UnsupportMonitorMetricsOperationException("不支持的操作系统");
		}
	}
	
	public abstract Object doMonitor(Map<String, Object> params) throws SigarException;
	
}
