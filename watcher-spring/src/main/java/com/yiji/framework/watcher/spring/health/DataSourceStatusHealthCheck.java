/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-19 21:58 创建
 *
 */
package com.yiji.framework.watcher.spring.health;

import java.sql.Connection;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;

import com.codahale.metrics.health.HealthCheck;
import com.yiji.framework.watcher.spring.SpringApplicationContextHolder;

/**
 * @author qiubo@yiji.com
 */
public class DataSourceStatusHealthCheck extends HealthCheck {
	
	@Override
	protected Result check() throws Exception {
		ApplicationContext context = SpringApplicationContextHolder.get();
		if (context == null) {
			return Result.unhealthy("applicationContext not found");
		}
		Map<String, DataSource> dataSources = context.getBeansOfType(DataSource.class, false, false);
		if (dataSources == null || dataSources.size() == 0) {
			return Result.unhealthy("dataSource not found");
		}
		for (Map.Entry<String, DataSource> entry : dataSources.entrySet()) {
			DataSource dataSource = entry.getValue();
			try (Connection connection = dataSource.getConnection()) {
				int timeout = 2;
				if (connection.isValid(timeout)) {
					return Result.healthy();
				} else {
					return Result.unhealthy("database is invalid with timeout " + timeout + " second");
				}
			} catch (Exception e) {
				Result.unhealthy(e.getMessage());
			}
		}
		return Result.healthy();
	}
}