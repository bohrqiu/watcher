/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-18 00:53 创建
 *
 */
package com.yiji.framework.watcher.metrics.base;

import java.util.List;
import java.util.Map;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.yiji.framework.watcher.Constants;

/**
 * @author qiubo@yiji.com
 */
public abstract class AbstractCachedWatcherMetrics extends AbstractWatcherMetrics {
	private static final List<String> DEFAULT_PARAM_KEY = ImmutableList.of(Constants.RES_TYPE_KEY, Constants.ACTION_KEY);
	private CacheTime cacheTime;
	
	public final Object watch(Map<String, Object> params) {
		try {
			if (cacheTime == null) {
				cacheTime = getCacheTime();
			}
			if (isCacheNotEnable()) {
				return doMonitor(params);
			} else {
				String key = buildKey(params);
				Object result = MetricsCache.INSTANCE.get(key);
				if (result == null) {
					result = doMonitor(params);
					if (result == null) {
						result = Constants.NULL;
					}
					MetricsCache.INSTANCE.put(key, result, cacheTime.getCacheTime());
					return result;
				} else {
					return result;
				}
			}
		} catch (Throwable throwable) {
			throw Throwables.propagate(throwable);
		}
	}
	

	private String buildKey(Map<String, Object> params) {
		StringBuilder key = new StringBuilder(this.getClass().getName());
		if (params == null || params.isEmpty()) {
			return key.toString();
		} else {
			for (String param : getParamsBuildKey()) {
				Object value = params.get(param);
				if (value == null) {
					key.append((String) null);
				} else {
					key.append(value.toString());
				}
			}
		}
		return key.toString();
	}
	
	/**
	 * 获取参与cache的key计算的参数名列表
	 * 
	 */
	protected List<String> getParamsBuildKey() {
		return DEFAULT_PARAM_KEY;
	}
	
	private boolean isCacheNotEnable() {
		return cacheTime == null || cacheTime == CacheTime.Never;
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
				return Long.MAX_VALUE;
			}
		};
		
		CacheTime FIVE_SECOND = new Time(5 * 1000);
		
		CacheTime THIRTY_SECOND = new Time(30 * 1000);
		
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
