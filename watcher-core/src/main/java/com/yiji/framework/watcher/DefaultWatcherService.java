/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-25 21:06 创建
 *
 */
package com.yiji.framework.watcher;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.yiji.framework.watcher.extension.ExtensionLoader;
import com.yiji.framework.watcher.marshaller.ResultMarshaller;
import com.yiji.framework.watcher.model.Request;
import com.yiji.framework.watcher.model.Result;

/**
 * @author qiubo@yiji.com
 */
public class DefaultWatcherService extends AbstractWatcherService {
	private static final Logger logger = LoggerFactory.getLogger(DefaultWatcherService.class);
	public static final DefaultWatcherService INSTANCE = new DefaultWatcherService();
	private Marshaller marshaller = new ResultMarshaller();
	
	private DefaultWatcherService() {
		ExtensionLoader loader = new ExtensionLoader();
		loader.load(this, WatcherMetrics.class);
	}
	
	public String watchAndMarshall(Request request) {
		return marshall(watch(request));
	}
	
	public Result watch(Request request) {
		logger.info("执行监控请求:action={},params={}", request.getAction(), request.getParams());
		try {
			Objects.requireNonNull(request, "request不能为空");
			WatcherMetrics watcherMetrics = monitorMetricsMap.get(request.getAction());
			if (watcherMetrics == null) {
				return Result.fail(request, "unsupport watch metrics:" + request.getAction());
			}
			request.addParam(Constants.RES_TYPE_KEY, request.getResponseType());
			Object result = watcherMetrics.watch(request.getParams());
			if (result == null || result == Constants.NULL) {
				return Result.fail(request, request.getAction() + " return null");
			} else {
				return Result.success(request, result);
			}
		} catch (Throwable e) {
			logger.info("执行错误,action={},params={}", request.getAction(), request.getParams(), e);
			if (Strings.isNullOrEmpty(e.getMessage())) {
				return Result.fail(request, Throwables.getStackTraceAsString(e));
			} else {
				return Result.fail(request, e.getMessage());
			}
		}
	}
	
	@Override
	public String marshall(Result result) {
		return marshaller.marshall(result);
	}
	
}
