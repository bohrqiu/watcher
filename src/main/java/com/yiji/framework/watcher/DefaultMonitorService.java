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

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Throwables;

/**
 * @author qiubo@yiji.com
 */
public class DefaultMonitorService extends AbstractMonitorService {
	private static final Logger logger = LoggerFactory.getLogger(DefaultMonitorService.class);
	public static final DefaultMonitorService INSTANCE = new DefaultMonitorService();
	
	private DefaultMonitorService() {
		MonitorMetricsLoader loader = new MonitorMetricsLoader();
		loader.loadMonitorMetrics(this);
	}
	
	public String monitor(MonitorRequest request) {
		logger.info("执行监控请求:action={},params={}", request.getAction(), request.getParams());
		try {
			Objects.requireNonNull(request, "request不能为空");
			MonitorMetrics monitorMetrics = monitorMetricsMap.get(request.getAction());
			if (monitorMetrics == null) {
				throw new MonitorMetricsOperationException("unsupport monitor metrics:" + request.getAction());
			}
			request.addParam(Constants.RES_TYPE_KEY, request.getResponseType());
			Object result = monitorMetrics.monitor(request.getParams());
			if (request.getResponseType() == ResponseType.TEXT) {
				if (result == null) {
					return "null";
				} else {
					return result.toString();
				}
			} else {
				return toJson(result, request.isPrettyFormat());
			}
		} catch (Throwable e) {
			logger.info("执行错误,MonitorRequest={}", request, e);
			if (request.getResponseType() == ResponseType.TEXT) {
				return Throwables.getStackTraceAsString(e);
			} else {
				ExceptionResult exceptionResult = new ExceptionResult();
				exceptionResult.setSuccess(false);
				exceptionResult.setMsg(e.getMessage());
				exceptionResult.setStackTrace(Throwables.getStackTraceAsString(e));
				return toJson(exceptionResult, request.isPrettyFormat());
			}
		}
	}
	
	private String toJson(Object result, boolean prettyFormat) {
		SerializeWriter out = null;
		try {
			
			if (prettyFormat) {
				out = new SerializeWriter(SerializerFeature.PrettyFormat);
			} else {
				out = new SerializeWriter();
			}
			JSONSerializer serializer = getJsonSerializer(out);
			serializer.write(result);
			return out.toString();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
	private JSONSerializer getJsonSerializer(SerializeWriter out) {
		JSONSerializer serializer = new JSONSerializer(out);
		serializer.config(SerializerFeature.WriteDateUseDateFormat, true);
		serializer.setDateFormat("yyyy-MM-dd HH:mm:ss");
		serializer.config(SerializerFeature.QuoteFieldNames, true);
		serializer.config(SerializerFeature.DisableCircularReferenceDetect, false);
		return serializer;
	}
	
	private static class ExceptionResult {
		private boolean success;
		private String msg;
		private String stackTrace;
		
		public String getMsg() {
			return msg;
		}
		
		public void setMsg(String msg) {
			this.msg = msg;
		}
		
		public String getStackTrace() {
			return stackTrace;
		}
		
		public void setStackTrace(String stackTrace) {
			this.stackTrace = stackTrace;
		}
		
		public boolean isSuccess() {
			return success;
		}
		
		public void setSuccess(boolean success) {
			this.success = success;
		}
	}
	
}
