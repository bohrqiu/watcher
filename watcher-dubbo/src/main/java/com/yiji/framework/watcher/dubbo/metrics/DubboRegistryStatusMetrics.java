/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-19 17:25 创建
 *
 */
package com.yiji.framework.watcher.dubbo.metrics;

import java.util.Collection;
import java.util.Map;

import com.alibaba.dubbo.registry.Registry;
import com.alibaba.dubbo.registry.support.AbstractRegistryFactory;
import com.google.common.collect.Maps;
import com.yiji.framework.watcher.WatcherException;

/**
 * @author qiubo@yiji.com
 */
public class DubboRegistryStatusMetrics extends AbstractDubboWatcherMetrics {
	@Override
	public Object doMonitor(Map<String, Object> params) throws Throwable {
		Map<String, Object> result = Maps.newHashMap();
		
		Collection<Registry> regsitries = AbstractRegistryFactory.getRegistries();
		if (regsitries.size() == 0) {
			throw WatcherException.throwIt("no registry found");
		}
		for (Registry registry : regsitries) {
			result.put(registry.getUrl().getAddress(), registry.isAvailable());
		}
		return result;
	}
	
	@Override
	public String name() {
		return "dubboRegistryStatus";
	}
	
	@Override
	public String desc() {
		return "dubbo registry status";
	}
	
	@Override
	public CacheTime getCacheTime() {
		return CacheTime.THIRTY_SECOND;
	}
}
