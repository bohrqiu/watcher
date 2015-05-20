/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-19 16:57 创建
 *
 */
package com.yiji.framework.watcher.dubbo.metrics;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.alibaba.dubbo.remoting.exchange.ExchangeServer;
import com.alibaba.dubbo.rpc.protocol.dubbo.DubboProtocol;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiji.framework.watcher.WatcherException;
import com.yiji.framework.watcher.dubbo.DubboDependencyChecker;
import com.yiji.framework.watcher.metrics.base.AbstractCachedWatcherMetrics;

/**
 * @author qiubo@yiji.com
 */
public class DubboServerStatusMetrics extends AbstractCachedWatcherMetrics {
	public DubboServerStatusMetrics() {
		new DubboDependencyChecker().check();
	}
	
	@Override
	public Object doMonitor(Map<String, Object> params) throws Throwable {
		List<Map<String, Object>> result = Lists.newArrayList();
		Collection<ExchangeServer> servers = DubboProtocol.getDubboProtocol().getServers();
		if (servers == null || servers.size() == 0) {
			throw WatcherException.throwIt("no server found");
		}
		for (ExchangeServer server : servers) {
			Map<String, Object> serverResult = Maps.newHashMap();
			boolean isBound = server.isBound();
			serverResult.put("isBound", isBound);
			if (isBound) {
				serverResult.put("clients", server.getChannels().size());
			}
			serverResult.put("port", server.getLocalAddress().getPort());
			result.add(serverResult);
		}
		return result;
	}
	
	@Override
	public String name() {
		return "dubboServerStatus";
	}
	
	@Override
	public String desc() {
		return "dubbo server status";
	}
	
	@Override
	public CacheTime getCacheTime() {
		return CacheTime.THIRTY_SECOND;
	}
}
