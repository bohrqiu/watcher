/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-18 13:39 创建
 *
 */
package com.yiji.framework.watcher.metrics.base;

import java.util.Map;
import java.util.concurrent.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.MapMaker;

/**
 * @author qiubo@yiji.com
 */
public class MetricsCache {
	private static final Logger logger = LoggerFactory.getLogger(MetricsCache.class);
	
	private static ConcurrentMap<String, CreateTimeWrapperedValue> cache = new MapMaker().concurrencyLevel(4).weakValues().makeMap();
	private static volatile ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
		@Override
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r);
			thread.setDaemon(true);
			thread.setName("watcher-cache-evict-thread");
			return thread;
		}
	});
	public static final MetricsCache INSTANCE = new MetricsCache();
	static {
		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				INSTANCE.evict();
			}
		}, 1l, 1l, TimeUnit.SECONDS);
	}
	
	private MetricsCache() {
		
	}
	
	public void put(String key, Object value, long expire) {
		logger.debug("cache put:key={},value={},expire={}", key, value, expire);
		cache.put(key, new CreateTimeWrapperedValue(value, expire));
	}
	
	public Object get(String key) {
		logger.debug("cache get:key={}", key);
		CreateTimeWrapperedValue value = cache.get(key);
		if (value == null) {
			return null;
		} else {
			return value.getValue();
		}
	}
	
	private void evict() {
		long now = System.currentTimeMillis();
		for (Map.Entry<String, CreateTimeWrapperedValue> valueEntry : cache.entrySet()) {
			if (valueEntry.getValue().expired(now)) {
				cache.remove(valueEntry.getKey());
				logger.debug("remove key:{}", valueEntry.getKey());
			}
		}
	}
	
	public void clear() {
		cache.clear();
	}
	
	private static class CreateTimeWrapperedValue {
		private long createTime;
		long expire;
		private Object value;
		
		public CreateTimeWrapperedValue(Object value, long expire) {
			this.value = value;
			this.expire = expire;
			this.createTime = System.currentTimeMillis();
		}
		
		public long getCreateTime() {
			return createTime;
		}
		
		public void setCreateTime(long createTime) {
			this.createTime = createTime;
		}
		
		public Object getValue() {
			return value;
		}
		
		public void setValue(Object value) {
			this.value = value;
		}
		
		public long getExpire() {
			return expire;
		}
		
		public void setExpire(long expire) {
			this.expire = expire;
		}
		
		public boolean expired() {
			return expire != Long.MAX_VALUE && expired(System.currentTimeMillis());
		}
		
		public boolean expired(long now) {
			return expire != Long.MAX_VALUE && now - createTime >= expire;
		}
	}
}
