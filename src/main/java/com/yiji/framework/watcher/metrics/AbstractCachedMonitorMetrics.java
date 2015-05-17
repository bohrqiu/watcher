/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-18 00:53 创建
 *
 */
package com.yiji.framework.watcher.metrics;

import java.util.Map;

/**
 * @author qiubo@yiji.com
 */
public abstract class AbstractCachedMonitorMetrics extends AbstractRateLimiterMonitorMetrics {
	private long lastAccessTime = 0l;
	private CacheTime cacheTime;
	private Throwable lastThrowable;
	
	@Override
	public final Object doRateLimiterMonitor(Map<String, Object> params) throws Throwable {
		long now = System.currentTimeMillis();
		try {
			//load cacheTime
			if (cacheTime == null) {
				cacheTime = getCacheTime();
			}
			
			//first access
			if (lastAccessTime == 0) {
				//if cache isn't enable,cache nothing
				if (isCacheNotEnable()) {
					return doMonitor(params);
				} else {
					return moniter(params);
				}
			}
			
			if (isCacheNotEnable()) {
				//never cache
				//if cache isn't enable,cache nothing
				return doMonitor(params);
			} else if (isAlwaysCache()) {
				//cache forver
				return getResultFromCache();
			} else {
				long elaspe = now - lastAccessTime;
				if (elaspe > cacheTime.getCacheTime()) {
					return moniter(params);
				} else {
					return getResultFromCache();
				}
			}
		} finally {
			lastAccessTime = now;
		}
	}
	
	private boolean isAlwaysCache() {
		return cacheTime == CacheTime.Forver;
	}
	
	private boolean isCacheNotEnable() {
		return cacheTime == null || cacheTime == CacheTime.Never;
	}
	
	private Object moniter(Map<String, Object> params) throws Throwable {
		try {
			lastResult = doMonitor(params);
			lastThrowable = null;
		} catch (Throwable e) {
			lastThrowable = e;
			throw e;
		}
		return lastResult;
	}
	
	private Object getResultFromCache() throws Throwable {
		if (lastThrowable != null) {
			throw lastThrowable;
		}
		return lastResult;
	}
	
	/**
	 * 结果缓存时间
	 */
	public interface CacheTime {
		/**
		 * 获取结果缓存时间，单位ms
		 */
		long getCacheTime();
		
		/**
		 * 结果永远不缓存
		 */
		CacheTime Never = new CacheTime() {
			@Override
			public long getCacheTime() {
				return 0;
			}
		};
		/**
		 * 结果永远缓存
		 */
		CacheTime Forver = new CacheTime() {
			@Override
			public long getCacheTime() {
				return -1;
			}
		};
		
		class Time implements CacheTime {
			private long cacheTime;
			
			public Time(long cacheTime) {
				this.cacheTime = cacheTime;
			}
			
			@Override
			public long getCacheTime() {
				return cacheTime;
			}
		}
		
	}
	
	/**
	 * 返回缓存时间
	 */
	public CacheTime getCacheTime() {
		return CacheTime.Never;
	}
	
	public abstract Object doMonitor(Map<String, Object> params) throws Throwable;
	
}
