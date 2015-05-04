/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-04 18:33 创建
 *
 */
package com.yiji.framework.watcher.metrics.os;

import java.util.List;
import java.util.Map;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.shell.ShellCommandExecException;
import org.hyperic.sigar.shell.ShellCommandUsageException;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qiubo@yiji.com
 */
public class IostatMetircs extends AbstractOSMonitorMetrics {
	private static final Logger logger = LoggerFactory.getLogger(IostatMetircs.class);

	@Override
	public Object doMonitor(Map<String, Object> params) throws SigarException {
		
		List<String> result = Lists.newArrayList();
		
		WatcherIostat iostat = new WatcherIostat();
		try {
			iostat.processCommand(new String[0]);
			result = iostat.getResult();
		} catch (ShellCommandUsageException e) {
            logger.error("执行iostat指标错误:", e);
			result.add(Throwables.getStackTraceAsString(e));
		} catch (ShellCommandExecException e) {
            logger.error("执行iostat指标错误:", e);
			result.add(Throwables.getStackTraceAsString(e));
		}
		if (isResponseText(params)) {
			if (result.isEmpty()) {
				return "null";
			} else {
				StringBuilder sb = new StringBuilder();
				for (String s : result) {
					sb.append(s).append("\n");
				}
				return sb.toString();
			}
		}
		return result;
	}
	
	@Override
	public String name() {
		return "iostat";
	}
	
	@Override
	public String desc() {
		return "io usage";
	}
}
