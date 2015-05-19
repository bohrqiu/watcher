/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-20 00:05 创建
 *
 */
package com.yiji.framework.watcher;

import com.yiji.framework.watcher.model.Result;

/**
 * 转换MonitorResult对象为字符串
 * @author qiubo@yiji.com
 */
public interface Marshaller {
	String marshall(Result result);
}
