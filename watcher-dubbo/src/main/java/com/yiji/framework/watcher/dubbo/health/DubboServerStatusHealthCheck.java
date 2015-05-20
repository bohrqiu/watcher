/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-19 16:43 创建
 *
 */
package com.yiji.framework.watcher.dubbo.health;

import java.util.Collection;

import com.alibaba.dubbo.remoting.exchange.ExchangeServer;
import com.alibaba.dubbo.rpc.protocol.dubbo.DubboProtocol;
import com.codahale.metrics.health.HealthCheck;
import com.yiji.framework.watcher.dubbo.DubboDependencyChecker;

/**
 * @author qiubo@yiji.com
 */
public class DubboServerStatusHealthCheck extends HealthCheck {
	public DubboServerStatusHealthCheck() {
		new DubboDependencyChecker().check();
	}
	
	@Override
	protected Result check() throws Exception {
		Collection<ExchangeServer> servers = DubboProtocol.getDubboProtocol().getServers();
		if (servers == null || servers.size() == 0) {
			return Result.unhealthy("no exchangeServer found");
		}
		for (ExchangeServer server : servers) {
			if (!server.isBound()) {
				return Result.unhealthy("server is unbound");
			}
		}
		return Result.healthy();
	}
}
