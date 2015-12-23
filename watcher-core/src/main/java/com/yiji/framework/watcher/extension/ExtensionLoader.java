/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-17 20:59 创建
 *
 */
package com.yiji.framework.watcher.extension;

import java.lang.reflect.Modifier;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import com.google.common.reflect.ClassPath;
import com.yiji.framework.watcher.Constants;
import com.yiji.framework.watcher.Utils;
import com.yiji.framework.watcher.WatcherDependencyNotFoundException;

/**
 * 扩展加载器，默认从{@link ExtensionLoader#getDefaultScanPackage()}包中和java spi机制中加载扩展
 * @author qiubo@yiji.com
 */
public class ExtensionLoader {
	private static final Logger logger = LoggerFactory.getLogger(ExtensionLoader.class);
	
	/**
	 * 加载扩展类到仓储中
	 * @param repository 扩展类存储仓储
	 * @param extensionType 扩展类类型
	 * @param <T> 类型
	 */
	public <T> void load(ExtensionRepository<T> repository, Class<T> extensionType) {
//		loadExtensionFromPackage(repository, extensionType, getDefaultScanPackage());
//		String customScanPackage = getCustomScanPackage();
//		if (!Strings.isNullOrEmpty(customScanPackage)) {
//			loadExtensionFromPackage(repository, extensionType, customScanPackage);
//		}
		loadMetricsFromSPI(repository, extensionType);
	}
	
	@VisibleForTesting
	<T> void loadExtensionFromPackage(ExtensionRepository<T> repository, Class<T> extensionType, String scanPackage) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try {
			Set<ClassPath.ClassInfo> classInfos = ClassPath.from(classLoader).getTopLevelClassesRecursive(scanPackage);
			for (ClassPath.ClassInfo classInfo : classInfos) {
				String clazzName = classInfo.getName();
				Class clazz;
				try {
					clazz = classLoader.loadClass(clazzName);
					if (extensionType.isAssignableFrom(clazz) && !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
						T extension;
						extension = (T) clazz.newInstance();
						logger.info("load extension:{}", extension.getClass().getName());
						repository.add(extension);
					}
				} catch (WatcherDependencyNotFoundException e) {
					logger.info("load extension {} error,cause of {} dont exists", clazzName, e.getMessage());
				} catch (Exception e) {
					logger.warn("load extension error:{}", clazzName, e);
				} catch (NoClassDefFoundError e) {
					logger.info("scan class:{} error:NoClassDefFoundError-{},this excepiton can be ignore", clazzName, e.getMessage());
				}
			}
		} catch (Exception e) {
			logger.error("load extension error", e);
		}
	}
	
	@VisibleForTesting
	<T> void loadMetricsFromSPI(ExtensionRepository<T> repository, Class<T> type) {
		ServiceLoader<T> loaders = ServiceLoader.load(type);
		Iterator<T> iterator = loaders.iterator();
		while (iterator.hasNext()) {
			try {
				T extension = iterator.next();
				repository.add(extension);
				logger.info("load extension:{}", extension.getClass().getName());
			} catch (Exception | ServiceConfigurationError e) {
				logger.warn("load extension from spi error:{}", e.getMessage());
			}
		}
	}
	
	@VisibleForTesting
	String getDefaultScanPackage() {
		String curPackage = ExtensionLoader.class.getPackage().getName();
		return curPackage.substring(0, curPackage.lastIndexOf('.'));
	}
	
	@VisibleForTesting
	String getCustomScanPackage() {
		String scanPackage = null;
		String sysProp = (String) System.getProperties().get(Constants.WATCHER_SCAN_PACKAGE);
		if (!Strings.isNullOrEmpty(sysProp)) {
			scanPackage = sysProp;
		}
		try {
			Map<Object, Object> props = Utils.getProperties(Constants.WATCHER_CONFIG_LOCATION);
			String configProp = (String) props.get(Constants.WATCHER_SCAN_PACKAGE);
			if (!Strings.isNullOrEmpty(configProp)) {
				scanPackage = configProp;
			}
		} catch (Exception e) {
			logger.info("watcher 配置文件:{} 不存在", Constants.WATCHER_CONFIG_LOCATION);
		}
		return scanPackage;
	}
}
