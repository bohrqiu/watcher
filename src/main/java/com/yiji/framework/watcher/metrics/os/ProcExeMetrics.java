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

import com.yiji.framework.watcher.MonitorMetrics;
import com.yiji.framework.watcher.UnsupportMonitorMetricsOperationException;
import org.hyperic.sigar.ProcExe;


/**
 * @author qzhanbo@yiji.com
 */
public class ProcExeMetrics implements MonitorMetrics {
	private Map result;
	

	public Object monitor(Map<String, Object> params) {
		if (result != null) {
			return result;
		}
		try {
			Map tmp = new HashMap<>();
			ProcExe procExe = SigarFactory.getSigar().getProcExe(SigarFactory.getSigar().getPid());
			tmp.put("cmd", procExe.getCwd());
			tmp.put("name", procExe.getName());
			tmp.put("inputArguments", ManagementFactory.getRuntimeMXBean().getInputArguments());
			result = tmp;
			return result;
		} catch (Exception e) {
			throw new UnsupportMonitorMetricsOperationException(e);
		}
		
	}
	

	public String name() {
		return "procExe";
	}

	public String desc() {
		return "程序启动命令相关信息";
	}
}
