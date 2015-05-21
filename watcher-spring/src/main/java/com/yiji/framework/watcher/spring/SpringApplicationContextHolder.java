/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-19 18:53 创建
 *
 */
package com.yiji.framework.watcher.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 此module的扩展通过此类获取到spring ApplicationContext，使用时，需要把此类配置到spring容器中
 * @author qiubo@yiji.com
 */
public class SpringApplicationContextHolder implements ApplicationContextAware {
	
	private static ApplicationContext CONTEXT;
	
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		CONTEXT = context;
	}
	
	public static ApplicationContext get() {
		return CONTEXT;
	}
	
}