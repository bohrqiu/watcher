/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-19 22:17 创建
 *
 */
package com.yiji.framework.watcher.spring.metrics;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiji.framework.watcher.OperationException;
import com.yiji.framework.watcher.metrics.base.AbstractCachedWatcherMetrics;
import com.yiji.framework.watcher.spring.SpringApplicationContextHolder;

/**
 * @author qiubo@yiji.com
 */
public class DataSourceMetics extends AbstractCachedWatcherMetrics {
	private static final Logger logger = LoggerFactory.getLogger(DataSourceMetics.class);
	
	@Override
	public Object doMonitor(Map<String, Object> params) throws Throwable {
		List<Map<String, Object>> result = Lists.newArrayList();
		ApplicationContext context = SpringApplicationContextHolder.get();
		if (context == null) {
			throw OperationException.throwIt("no applicationContext found");
		}
		Map<String, DataSource> dataSources = context.getBeansOfType(DataSource.class, false, false);
		if (dataSources == null || dataSources.size() == 0) {
			throw OperationException.throwIt("no dataSource found");
		}
		for (Map.Entry<String, DataSource> entry : dataSources.entrySet()) {
			Map<String, Object> dataSourceResult = Maps.newHashMap();
			DataSource dataSource = entry.getValue();
			dataSourceResult.put("name", entry.getKey());
			try {
				try (Connection connection = dataSource.getConnection()) {
					DatabaseMetaData metaData = connection.getMetaData();
					dataSourceResult.put("connectionURL", metaData.getURL());
					dataSourceResult.put("databaseProductName", metaData.getDatabaseProductName());
					dataSourceResult.put("databaseProductVersion", metaData.getDatabaseProductVersion());
					dataSourceResult.put("userName", metaData.getUserName());
					
					dataSourceResult.put("driverName", metaData.getDriverName());
					dataSourceResult.put("driverVersion", metaData.getDriverVersion());
					dataSourceResult.put("usesLocalFilePerTable", metaData.usesLocalFilePerTable());
					dataSourceResult.put("driverVersion", metaData.getDriverVersion());
					dataSourceResult.put("maxConnections", metaData.getMaxConnections());
				}
			} catch (Throwable e) {
				logger.error(e.getMessage(), e);
				throw OperationException.throwIt(e);
			}
			result.add(dataSourceResult);
		}
		return result;
	}
	
	@Override
	public CacheTime getCacheTime() {
		return CacheTime.Forver;
	}
	
	@Override
	public String name() {
		return "datasource";
	}
	
	@Override
	public String desc() {
		return "datasource";
	}
}
