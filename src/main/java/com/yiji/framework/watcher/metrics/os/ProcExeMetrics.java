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

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

import org.hyperic.sigar.ProcExe;
import org.hyperic.sigar.SigarException;

/**
 * @author qiubo@yiji.com
 */
public class ProcExeMetrics extends AbstractOSMonitorMetrics {
	private Map result;
	
	public Object doMonitor(Map<String, Object> params) throws SigarException {
		if (result != null) {
			return result;
		}
		Map tmp = new HashMap<>();
		ProcExe procExe = SigarFactory.getSigar().getProcExe(SigarFactory.getSigar().getPid());
		tmp.put("cmd", procExe.getCwd());
		tmp.put("name", procExe.getName());
		tmp.put("inputArguments", ManagementFactory.getRuntimeMXBean().getInputArguments());
		result = tmp;
		return result;
	}
	
	public String name() {
		return "procExe";
	}
	
	public String desc() {
		return "show process starting command and arguments";
	}
}
