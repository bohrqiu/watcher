/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-25 22:21 创建
 *
 */
package com.yiji.framework.watcher.metrics.os;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

import org.hyperic.sigar.ProcExe;

/**
 * @author qiubo@yiji.com
 */
public class ProcExeMetrics extends AbstractOSWatcherMetrics {
	
	public Object doMonitor(Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<>();
		ProcExe procExe = SigarFactory.getSigar().getProcExe(SigarFactory.getSigar().getPid());
		result.put("cmd", procExe.getCwd());
		result.put("name", procExe.getName());
		result.put("inputArguments", ManagementFactory.getRuntimeMXBean().getInputArguments());
		return result;
	}
	
	@Override
	public CacheTime getCacheTime() {
		return CacheTime.Forver;
	}
	
	public String name() {
		return "procExe";
	}
	
	public String desc() {
		return "show process starting command and arguments";
	}
}
