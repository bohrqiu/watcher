/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-04-25 21:06 创建
 *
 */
package com.yiji.framework.watcher;

import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Throwables;
import com.google.common.reflect.ClassPath;

/**
 * @author qzhanbo@yiji.com
 */
public class DefaultMonitorService extends AbstractMonitorService {
	private static final Logger logger = LoggerFactory.getLogger(DefaultMonitorService.class);
	public static final DefaultMonitorService INSTANCE = new DefaultMonitorService();

	private DefaultMonitorService() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try {
			Set<ClassPath.ClassInfo> classInfos = ClassPath.from(classLoader).getTopLevelClassesRecursive("com.yiji.framework.watcher.metrics");
			for (ClassPath.ClassInfo classInfo : classInfos) {
				String clazzName = classInfo.getName();
				try {
					Class clazz = null;
					try {
						clazz = classLoader.loadClass(clazzName);
					} catch (Exception e) {
						logger.debug("{}加载失败,原因:{}", clazzName, e.getMessage());
						return;
					}
					if (MonitorMetrics.class.isAssignableFrom(clazz) && !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
						MonitorMetrics monitorMetrics = null;
						try {
							monitorMetrics = (MonitorMetrics) clazz.newInstance();
							logger.debug("监控注册:{}->{}", monitorMetrics.name(), clazzName);
							DefaultMonitorService.this.addMonitorMetrics(monitorMetrics);
						} catch (Exception e) {
							logger.debug("{}初始化失败,原因:{}", clazzName, e.getMessage());
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
	
	public String monitor(MonitorRequest request) {
		
		try {
			Objects.requireNonNull(request, "request不能为空");
			MonitorMetrics monitorMetrics = monitorMetricsMap.get(request.getAction());
			if (monitorMetrics == null) {
				throw new UnsupportMonitorMetricsOperationException("不支持的监控" + request.getAction());
			}
			request.addParam(ResponseType.RESPONSE_TYPE_KEY, request.getResponseType());
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
		} catch (Exception e) {
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
