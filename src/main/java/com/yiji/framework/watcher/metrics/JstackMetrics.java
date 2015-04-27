/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-04-25 15:59 创建
 *
 */
package com.yiji.framework.watcher.metrics;

import com.yiji.framework.watcher.MonitorMetrics;
import com.yiji.framework.watcher.ResponseType;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.*;
import java.util.Map;



/**
 * @author qzhanbo@yiji.com
 */
public class JstackMetrics implements MonitorMetrics {
	private ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
	

	public Object monitor(Map<String, Object> params) {
		ResponseType responseType = (ResponseType) params.get(ResponseType.RESPONSE_TYPE_KEY);
		if(responseType==ResponseType.TEXT){
			return dump();
		}
		return getThreadInfos();
	}
	

	public String name() {
		return "jstack";
	}


	public String desc() {
		return "java线程栈";
	}

	private ThreadInfo[] getThreadInfos() {
		return this.threadMXBean.dumpAllThreads(true, true);
	}
	
	public String dump() {
		final ThreadInfo[] threads = getThreadInfos();
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		
		for (int ti = threads.length - 1; ti >= 0; ti--) {
			final ThreadInfo t = threads[ti];
			writer.printf("%s id=%d state=%s", t.getThreadName(), t.getThreadId(), t.getThreadState());
			final LockInfo lock = t.getLockInfo();
			if (lock != null && t.getThreadState() != Thread.State.BLOCKED) {
				writer.printf("%n    - waiting on <0x%08x> (a %s)", lock.getIdentityHashCode(), lock.getClassName());
				writer.printf("%n    - locked <0x%08x> (a %s)", lock.getIdentityHashCode(), lock.getClassName());
			} else if (lock != null && t.getThreadState() == Thread.State.BLOCKED) {
				writer.printf("%n    - waiting to lock <0x%08x> (a %s)", lock.getIdentityHashCode(), lock.getClassName());
			}
			
			if (t.isSuspended()) {
				writer.print(" (suspended)");
			}
			
			if (t.isInNative()) {
				writer.print(" (running in native)");
			}
			
			writer.println();
			if (t.getLockOwnerName() != null) {
				writer.printf("     owned by %s id=%d%n", t.getLockOwnerName(), t.getLockOwnerId());
			}
			
			final StackTraceElement[] elements = t.getStackTrace();
			final MonitorInfo[] monitors = t.getLockedMonitors();
			
			for (int i = 0; i < elements.length; i++) {
				final StackTraceElement element = elements[i];
				writer.printf("    at %s%n", element);
				for (int j = 1; j < monitors.length; j++) {
					final MonitorInfo monitor = monitors[j];
					if (monitor.getLockedStackDepth() == i) {
						writer.printf("      - locked %s%n", monitor);
					}
				}
			}
			writer.println();
			
			final LockInfo[] locks = t.getLockedSynchronizers();
			if (locks.length > 0) {
				writer.printf("    Locked synchronizers: count = %d%n", locks.length);
				for (LockInfo l : locks) {
					writer.printf("      - %s%n", l);
				}
				writer.println();
			}
		}
		
		writer.println();
		writer.flush();
		return sw.toString();
	}
	
}
