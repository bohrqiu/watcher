/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-27 12:57 创建
 *
 */
package com.yiji.framework.watcher.http.adaptor.web;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;
import com.yiji.framework.watcher.*;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import com.yiji.framework.watcher.metrics.base.MetricsCache;
import com.yiji.framework.watcher.model.Request;
import com.yiji.framework.watcher.Constants;

/**
 * @author qiubo@yiji.com
 */
public class WatcherServlet extends AccessControlServlet {
	public static final String SYS_NAME = "";
	private static String velocityPath = "com/yiji/framework/watcher/http/adaptor/web/index.vm";
	private static VelocityEngine velocity;
	private static String vmContent = null;
	
	static {
		velocity = new VelocityEngine();
		//模板内引用解析失败时不抛出异常
		velocity.setProperty("runtime.references.strict", "false");
		velocity.init();
	}
	
	private String index = null;
	private String appName = null;
	
	public WatcherServlet() {
	}
	
	@Override
	public void init() throws ServletException {
		super.init();
		initAppName();
	}
	
	private void initAppName() {
		if (Strings.isNullOrEmpty(this.appName)) {
			String appName = System.getProperty(Constants.WATCHER_APP_NAME);
			if (Strings.isNullOrEmpty(appName)) {
				appName = getInitParameter(Constants.WATCHER_APP_NAME);
				if (!Strings.isNullOrEmpty(appName)) {
					this.appName = appName;
				}
			} else {
				this.appName = appName;
			}
		}
		System.setProperty(Constants.WATCHER_APP_NAME, appName);
	}
	
	/**
	 * @param appName 应用名称
	 */
	public WatcherServlet(String appName) {
		this.appName = appName;
        System.setProperty(Constants.WATCHER_APP_NAME, appName);
    }
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		final String uri = req.getPathInfo();
		if (uri == null) {
			resp.sendRedirect(req.getRequestURI() + "/");
			return;
		}
		if (uri.equals("/") || uri.equals("/index.html") || uri.equals("/index.htm")) {
			handleIndex(req, resp);
		} else if (uri.contains("q.do")) {
			Map<String, Object> paramMap = getRequestParamMap(req);
			Request request = new Request();
			request.setParams(paramMap);
			String action = (String) paramMap.get("action");
			if (action == null) {
				resp.getWriter().write("请指定action参数值");
				return;
			}
			request.setAction(paramMap.get("action").toString());
			setPrettyFormat(paramMap, request);
			setResType(paramMap, request);
			if (request.getResponseType() == Constants.ResponseType.JSON) {
				if (Utils.isIE(req.getHeader("user-agent"))) {
					resp.setContentType("text/plain;charset=utf-8");
					resp.setHeader("Damned internet explorer", "Do you agree?");
				} else {
					resp.setContentType("application/json;charset=utf-8");
				}
			} else {
				resp.setContentType("text/plain;charset=utf-8");
			}
			resp.getWriter().write(DefaultWatcherService.INSTANCE.watchAndMarshall(request));
		} else {
			resp.getWriter().write("不支持的请求");
		}
	}
	
	private void handleIndex(HttpServletRequest req, HttpServletResponse resp) {
		try {
			Map<String, Object> params = Maps.newHashMap();
			params.put("appName", appName);
			params.put("metricses", DefaultWatcherService.INSTANCE.set());
			if (vmContent == null) {
				vmContent = readFile(velocityPath);
			}
			index = parseVelocity(vmContent, params);
		} catch (Exception e) {
			index = Throwables.getStackTraceAsString(e);
		}
		try {
			resp.getWriter().write(index);
		} catch (IOException e) {
			//do nothing
		}
	}
	
	private Map<String, Object> getRequestParamMap(HttpServletRequest req) {
		Map<String, Object> paramMap = Maps.newHashMap();
		Map<String, String[]> stringMap = req.getParameterMap();
		for (Map.Entry<String, String[]> stringEntry : stringMap.entrySet()) {
			if (stringEntry.getValue() != null) {
				paramMap.put(stringEntry.getKey(), stringEntry.getValue()[0]);
			}
		}
		return paramMap;
	}
	
	private void setPrettyFormat(Map<String, Object> paramMap, Request request) {
		Object prettyFormat = paramMap.get("prettyFormat");
		if (prettyFormat != null) {
			request.setPrettyFormat(Boolean.parseBoolean(prettyFormat.toString()));
		}
	}
	
	private void setResType(Map<String, Object> paramMap, Request request) {
		try {
			Object resType = paramMap.get("resType");
			if (resType == null) {
				request.setResponseType(Constants.ResponseType.JSON);
			} else {
				String str = resType.toString().toUpperCase();
				request.setResponseType(Constants.ResponseType.valueOf(str));
			}
		} catch (IllegalArgumentException e) {
			request.setResponseType(Constants.ResponseType.JSON);
		}
	}
	
	private static String readFile(String resourceLocation) {
		try {
			URL url = Resources.getResource(resourceLocation);
			return Resources.toString(url, Charsets.UTF_8);
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}
	
	private static String parseVelocity(String templateContent, Map<String, Object> param) {
		VelocityContext context = new VelocityContext(param);
		StringWriter w = new StringWriter();
		velocity.evaluate(context, w, velocityPath, templateContent);
		return w.toString();
	}
	
	@Override
	public void destroy() {
		super.destroy();
		MetricsCache.stop();
	}
}
