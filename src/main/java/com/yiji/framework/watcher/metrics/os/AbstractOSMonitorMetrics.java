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

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yiji.framework.watcher.UnsupportMonitorMetricsOperationException;
import com.yiji.framework.watcher.metrics.AbstractMonitorMetrics;

/**
 * @author qiubo@yiji.com
 */
public abstract class AbstractOSMonitorMetrics extends AbstractMonitorMetrics {
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractOSMonitorMetrics.class);
	
	protected Sigar sigar = SigarFactory.getSigar();
	
	public Object monitor(Map<String, Object> params) {
		try {
			return doMonitor(params);
		} catch (Exception e) {
			logger.error("执行os监控指标错误:", e);
			throw new UnsupportMonitorMetricsOperationException(e);
		} catch (UnsatisfiedLinkError error) {
			logger.error("执行os监控指标错误:", error);
			throw new UnsupportMonitorMetricsOperationException("不支持的操作系统");
		}
	}
	
	public abstract Object doMonitor(Map<String, Object> params) throws SigarException;
	
}
