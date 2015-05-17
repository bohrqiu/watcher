/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-17 20:59 创建
 *
 */
package com.yiji.framework.watcher;

import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.reflect.ClassPath;

/**
 * MonitorMetrics加载器，默认从
 * @author qiubo@yiji.com
 */
public class MonitorMetricsLoader {
	private static final Logger logger = LoggerFactory.getLogger(MonitorMetricsLoader.class);
	
	public void loadMonitorMetrics(MonitorMetricsRepository repository) {
		loadMetricsFromDefaultPackage(repository);
		loadMetricsFromSPI(repository);
	}
	
	@VisibleForTesting
	void loadMetricsFromSPI(MonitorMetricsRepository repository) {
		ServiceLoader<MonitorMetrics> loaders = ServiceLoader.load(MonitorMetrics.class);
		Iterator<MonitorMetrics> iterator = loaders.iterator();
		while (iterator.hasNext()) {
			try {
				MonitorMetrics metrics = iterator.next();
				repository.addMonitorMetrics(metrics);
				logger.info("监控注册:{}->{}", metrics.name(), metrics.getClass().getName());
				
			} catch (Exception | ServiceConfigurationError e) {
				logger.warn("load MonitorMetrics from spi error", e);
			}
		}
	}
	
	@VisibleForTesting
	void loadMetricsFromDefaultPackage(MonitorMetricsRepository repository) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try {
			Set<ClassPath.ClassInfo> classInfos = ClassPath.from(classLoader).getTopLevelClassesRecursive(
				MonitorMetricsLoader.class.getPackage().getName());
			for (ClassPath.ClassInfo classInfo : classInfos) {
				String clazzName = classInfo.getName();
				try {
					Class clazz = null;
					try {
						clazz = classLoader.loadClass(clazzName);
					} catch (Exception e) {
						logger.warn("{}加载失败,原因:{}", clazzName, e.getMessage());
						return;
					}
					if (MonitorMetrics.class.isAssignableFrom(clazz) && !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
						MonitorMetrics monitorMetrics = null;
						try {
							monitorMetrics = (MonitorMetrics) clazz.newInstance();
							logger.info("监控注册:{}->{}", monitorMetrics.name(), clazzName);
							repository.addMonitorMetrics(monitorMetrics);
						} catch (Exception e) {
							logger.warn("{}初始化失败,原因:{}", clazzName, e.getMessage());
						}
					}
				} catch (Exception e) {
					logger.error("初始化错误", e);
				}
			}
		} catch (Exception e) {
			logger.error("初始化错误", e);
		}
	}
}
