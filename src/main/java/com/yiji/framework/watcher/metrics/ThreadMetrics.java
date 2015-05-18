/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-25 15:50 创建
 *
 */
package com.yiji.framework.watcher.metrics;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.yiji.framework.watcher.metrics.base.AbstractCachedMonitorMetrics;

/**
 * @author qiubo@yiji.com
 */
public class ThreadMetrics extends AbstractCachedMonitorMetrics {
	private static final int MAX_STACK_TRACE_DEPTH = 100;
	
	public Object doMonitor(Map<String, Object> params) {
		Map<String, Object> map = Maps.newHashMap();
		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
		map.put("threadCount", threadMXBean.getThreadCount());
		map.put("totalStartedThreadCount", threadMXBean.getTotalStartedThreadCount());
		map.put("deadlockedThreads", getDeadlockedThreads(threadMXBean));
		map.put("deadlockedThreadsCount", getDeadlockedThreadsCount(threadMXBean));
		map.put("peakThreadCount", threadMXBean.getPeakThreadCount());
		map.put("daemonThreadCount", threadMXBean.getDaemonThreadCount());
		return map;
	}
	
	@Override
	public CacheTime getCacheTime() {
		return CacheTime.THIRTY_SECOND;
	}
	
	private Set<String> getDeadlockedThreads(ThreadMXBean threads) {
		final long[] ids = threads.findDeadlockedThreads();
		if (ids != null) {
			final Set<String> deadlocks = new HashSet<>();
			for (ThreadInfo info : threads.getThreadInfo(ids, MAX_STACK_TRACE_DEPTH)) {
				final StringBuilder stackTrace = new StringBuilder();
				for (StackTraceElement element : info.getStackTrace()) {
					stackTrace.append("\t at ").append(element.toString()).append(String.format("%n"));
				}
				
				deadlocks.add(String.format("%s locked on %s (owned by %s):%n%s", info.getThreadName(), info.getLockName(), info.getLockOwnerName(),
					stackTrace.toString()));
			}
			return Collections.unmodifiableSet(deadlocks);
		}
		return Collections.emptySet();
	}
	
	private int getDeadlockedThreadsCount(ThreadMXBean threads) {
		final long[] ids = threads.findDeadlockedThreads();
		if (ids == null) {
			return 0;
		} else {
			return ids.length;
		}
	}
	
	public String name() {
		return "thread";
	}
	
	public String desc() {
		return "show thread stats";
	}
}
