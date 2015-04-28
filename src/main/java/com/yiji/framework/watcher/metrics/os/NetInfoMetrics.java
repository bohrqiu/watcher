/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-04-25 22:16 创建
 *
 */
package com.yiji.framework.watcher.metrics.os;

import java.util.Map;

import org.hyperic.sigar.SigarException;

/**
 * @author qzhanbo@yiji.com
 */
public class NetInfoMetrics extends AbstractOSMonitorMetrics {
	
	public Object doMonitor(Map<String, Object> params) throws SigarException {
		return SigarFactory.getSigar().getNetInfo().toMap();
	}
	
	public String name() {
		return "netinfo";
	}
	
	public String desc() {
		return "网络配置情况";
	}
}
