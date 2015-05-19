/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-19 18:58 创建
 *
 */
package com.yiji.framework.watcher.spring.health;

import org.springframework.context.ApplicationContext;
import org.springframework.context.Lifecycle;

import com.codahale.metrics.health.HealthCheck;
import com.yiji.framework.watcher.spring.SpringApplicationContextHolder;

/**
 * @author qiubo@yiji.com
 */
public class SpringStatusHealthCheck extends HealthCheck {
	
	@Override
	protected Result check() throws Exception {
		ApplicationContext context = SpringApplicationContextHolder.get();
		if (context == null) {
			return Result.unhealthy("applicationContext not found");
		}
		if (context instanceof Lifecycle) {
			if (((Lifecycle) context).isRunning()) {
				return Result.healthy();
			} else {
				return Result.unhealthy("applicationContext not runing");
			}
		}
		return Result.healthy();
	}
}
