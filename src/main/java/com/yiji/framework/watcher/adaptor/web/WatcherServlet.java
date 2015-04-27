/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-04-27 12:57 创建
 *
 */
package com.yiji.framework.watcher.adaptor.web;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.yiji.framework.watcher.DefaultMonitorService;

/**
 * @author qzhanbo@yiji.com
 */
public class WatcherServlet extends HttpServlet {
	private static String velocityPath = "/com/yiji/framework/watcher/adaptor/web/index.vm";
	
	private String index = null;
	private String appName = null;
	
	public WatcherServlet() {
	}
	
	public WatcherServlet(String appName) {
		this.appName = appName;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		final String uri = req.getPathInfo();
		if (uri == null || uri.equals("/")) {
			handleIndex(req, resp);
			return;
		} else {
		}
	}
	
	private void handleIndex(HttpServletRequest req, HttpServletResponse resp) {
		if (Strings.isNullOrEmpty(index)) {
			try {
				String file = resolveClasspath("/com/yiji/framework/watcher/adaptor/web/index.vm");
				if (file != null) {
					StringBuilder content = new StringBuilder();
					Files.readLines(new File(file), Charsets.UTF_8).stream().forEach(s -> content.append(s));
					Map<String, Object> params = Maps.newHashMap();
					params.put("appName", appName);
					params.put("metricsNames", DefaultMonitorService.INSTANCE.names());
					index = parseVelocity(content.toString(), params);
				} else {
					index = "监控主页文件不存在";
				}
			} catch (IOException e) {
				index = Throwables.getStackTraceAsString(e);
			}
		}
		try {
			resp.getWriter().write(index);
		} catch (IOException e) {
			//do nothing
		}
	}
	
	private static String resolveClasspath(String resourceLocation) {
		URL url = getDefaultClassLoader().getResource(resourceLocation);
		if (url == null) {
			return null;
		}
		return url.getFile();
		
	}
	
	private static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			//ignore
		}
		if (cl == null) {
			cl = WatcherServlet.class.getClassLoader();
		}
		return cl;
	}
	
	private static String parseVelocity(String templateContent, Map<String, Object> param) {
		VelocityContext context = new VelocityContext(param);
		StringWriter w = new StringWriter();
		VelocityEngine velocity = new VelocityEngine();
		//模板内引用解析失败时不抛出异常
		velocity.setProperty("runtime.references.strict", "false");
		velocity.init();
		velocity.evaluate(context, w, velocityPath, templateContent);
		return w.toString();
	}
}
