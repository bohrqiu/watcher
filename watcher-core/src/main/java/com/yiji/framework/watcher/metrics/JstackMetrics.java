/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-25 15:59 创建
 *
 */
package com.yiji.framework.watcher.metrics;

import com.google.common.collect.Lists;
import com.yiji.framework.watcher.metrics.base.AbstractCachedWatcherMetrics;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author qiubo@yiji.com
 */
public class JstackMetrics extends AbstractCachedWatcherMetrics {
	private ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
	
	public Object doMonitor(Map<String, Object> params) {
		if (isResponseText(params)) {
			return dump();
		}
		return getThreadInfos();
	}
	
	@Override
	public CacheTime getCacheTime() {
		return CacheTime.FIVE_SECOND;
	}
	
	public String dump() {
		final List<ThreadInfoWrapper> threads = getThreadInfos();
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		
		for (ThreadInfoWrapper threadInfoWrapper : threads) {
			final ThreadInfoWrapper t = threadInfoWrapper;
			writer.printf("%s id=%d state=%s deamon=%s priority=%s cpu[total=%sms,user=%sms]", t.getThreadName(),
				t.getThreadId(), t.getThreadState(), t.isDaemon(), t.getPriority(), t.getCpuTimeMillis(),
				t.getUserTimeMillis());
			final LockInfo lock = t.getLockInfo();
			if (lock != null && t.getThreadState() != Thread.State.BLOCKED) {
				writer.printf("%n    - waiting on <0x%08x> (a %s)", lock.getIdentityHashCode(), lock.getClassName());
				writer.printf("%n    - locked <0x%08x> (a %s)", lock.getIdentityHashCode(), lock.getClassName());
			} else if (lock != null && t.getThreadState() == Thread.State.BLOCKED) {
				writer.printf("%n    - waiting to lock <0x%08x> (a %s)", lock.getIdentityHashCode(),
					lock.getClassName());
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
	
	private List<ThreadInfoWrapper> getThreadInfos() {
		boolean cpuTimeEnabled = threadMXBean.isThreadCpuTimeSupported() && threadMXBean.isThreadCpuTimeEnabled();
		final Map<Thread, StackTraceElement[]> stackTraces = Thread.getAllStackTraces();
		final List<Thread> threads = new ArrayList<Thread>(stackTraces.keySet());
		List<ThreadInfoWrapper> result = Lists.newArrayList();
		for (Map.Entry<Thread, StackTraceElement[]> threadEntry : stackTraces.entrySet()) {
			Thread  thread=threadEntry.getKey();
			final ThreadInfo t = this.threadMXBean.getThreadInfo(thread.getId());
			final long cpuTimeMillis;
			final long userTimeMillis;
			if (cpuTimeEnabled) {
				cpuTimeMillis = threadMXBean.getThreadCpuTime(t.getThreadId()) / 1000000;
				userTimeMillis = threadMXBean.getThreadUserTime(t.getThreadId()) / 1000000;
			} else {
				cpuTimeMillis = -1;
				userTimeMillis = -1;
			}
			result
					.add(new ThreadInfoWrapper(t, cpuTimeMillis, userTimeMillis, thread.isDaemon(), thread.getPriority(),threadEntry.getValue()));
		}
		return result;
	}
	
	public static class ThreadInfoWrapper {
		
		private ThreadInfo threadInfo;
		private long cpuTimeMillis;
		private long userTimeMillis;
		private boolean daemon;
		private int priority;
		private StackTraceElement[] stackTrace;
		
		public ThreadInfoWrapper(ThreadInfo threadInfo, long cpuTimeMillis, long userTimeMillis, boolean deamon,
									int priority,StackTraceElement[] stackTrace) {
			this.threadInfo = threadInfo;
			this.daemon = deamon;
			this.cpuTimeMillis = cpuTimeMillis;
			this.userTimeMillis = userTimeMillis;
			this.priority = priority;
			this.stackTrace=stackTrace;
		}
		
		public long getBlockedCount() {
			return threadInfo.getBlockedCount();
		}
		
		public long getBlockedTime() {
			return threadInfo.getBlockedTime();
		}
		
		public MonitorInfo[] getLockedMonitors() {
			return threadInfo.getLockedMonitors();
		}
		
		public LockInfo[] getLockedSynchronizers() {
			return threadInfo.getLockedSynchronizers();
		}
		
		public LockInfo getLockInfo() {
			return threadInfo.getLockInfo();
		}
		
		public String getLockName() {
			return threadInfo.getLockName();
		}
		
		public long getLockOwnerId() {
			return threadInfo.getLockOwnerId();
		}
		
		public String getLockOwnerName() {
			return threadInfo.getLockOwnerName();
		}
		
		public StackTraceElement[] getStackTrace() {
			return stackTrace;
		}
		
		public long getThreadId() {
			return threadInfo.getThreadId();
		}
		
		public String getThreadName() {
			return threadInfo.getThreadName();
		}
		
		public Thread.State getThreadState() {
			return threadInfo.getThreadState();
		}
		
		public long getWaitedCount() {
			return threadInfo.getWaitedCount();
		}
		
		public long getWaitedTime() {
			return threadInfo.getWaitedTime();
		}
		
		public boolean isInNative() {
			return threadInfo.isInNative();
		}
		
		public boolean isSuspended() {
			return threadInfo.isSuspended();
		}
		
		public long getCpuTimeMillis() {
			return cpuTimeMillis;
		}
		
		public long getUserTimeMillis() {
			return userTimeMillis;
		}
		
		public boolean isDaemon() {
			return daemon;
		}
		
		public void setDaemon(boolean daemon) {
			this.daemon = daemon;
		}
		
		public int getPriority() {
			return priority;
		}
		
		public void setPriority(int priority) {
			this.priority = priority;
		}
	}
	
	public String name() {
		return "jstack";
	}
	
	public String desc() {
		return "print java stack trace.";
	}
	
}
