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
 * @author qiubo@yiji.com
 */
public class NetStatMetrics extends AbstractOSMonitorMetrics {
	
	public Object doMonitor(Map<String, Object> params) throws SigarException {
		return SigarFactory.getSigar().getNetStat();
	}
	
	public String name() {
		return "netstat";
	}
	
	public String desc() {
		return "network use stats";
	}
}
