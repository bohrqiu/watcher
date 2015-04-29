/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-04-25 22:21 创建
 *
 */
package com.yiji.framework.watcher.metrics.os;

import java.util.Map;

import org.hyperic.sigar.SigarException;

/**
 * @author qzhanbo@yiji.com
 */
public class SwapMetrics extends AbstractOSMonitorMetrics {
	
	public Object doMonitor(Map<String, Object> params) throws SigarException {
		return SigarFactory.getSigar().getSwap().toMap();
	}
	
	public String name() {
		return "swap";
	}
	
	public String desc() {
		return "os swap use stats";
	}
}
