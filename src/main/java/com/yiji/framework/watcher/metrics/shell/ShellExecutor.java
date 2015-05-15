/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-15 15:13 创建
 *
 */
package com.yiji.framework.watcher.metrics.shell;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

import org.apache.commons.exec.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.io.ByteStreams;
import com.yiji.framework.watcher.Utils;

/**
 * @author qiubo@yiji.com
 */
public class ShellExecutor {
	private static final Logger logger = LoggerFactory.getLogger(ShellExecutor.class);
	
	private static final String scriptRelativePath = "watcher" + File.separator + "script";
	private static final String scriptPath = new File(System.getProperty("java.io.tmpdir") + File.separator + scriptRelativePath).getPath();
	
	private static boolean init = false;
	
	private static String initMsg = null;
	
	public ShellExecutor() {
		if (!Utils.isLinux()) {
			initMsg = "仅支持linux操作系统";
		}
		if (!init) {
			logger.info("watcher script安装路径:{}", scriptPath);
			try {
				initScript();
				init = true;
			} catch (Exception e) {
				logger.info(e.getMessage(), e);
				initMsg = Throwables.getStackTraceAsString(e);
			}
		}
	}
	
	private void initScript() throws Exception {
		//创建脚本目录
		File dir = new File(scriptPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String path = scriptRelativePath;
		for (URL url : Utils.findResourceFromClassPath(path)) {
			if (url.getFile().endsWith(File.separator)) {
				File subDir = new File(scriptPath(url, path));
				if (!subDir.exists()) {
					subDir.mkdirs();
				}
			} else {
				String scriptPath = scriptPath(url, path);
				File scriptFile = new File(scriptPath);
				if (!scriptFile.exists()) {
					try (FileOutputStream out = new FileOutputStream(scriptFile)) {
						ByteStreams.copy(url.openStream(), out);
						logger.info("创建脚本文件:{}", scriptPath);
					}
				}
			}
		}
	}
	
	private String getRelativePath(URL url, String relativePath) {
		return url.getFile().substring(url.getFile().indexOf(relativePath) + relativePath.length());
	}
	
	private String scriptPath(URL url, String relativePath) {
		return scriptPath + File.separator + getRelativePath(url, relativePath);
	}
	
	public String exeShellConetent(String content) {
		if (!init) {
			return initMsg;
		}
		logger.info("执行脚本:{}", content);
		CommandLine cmdLine = CommandLine.parse(content);
		DefaultExecutor executor = new DefaultExecutor();
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(byteArrayOutputStream);
		executor.setStreamHandler(pumpStreamHandler);
		
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		//如果认为60s还没有执行完，停止认为执行
		ExecuteWatchdog watchdog = new ExecuteWatchdog(30000);
		executor.setWatchdog(watchdog);
		try {
			executor.execute(cmdLine, System.getenv(), resultHandler);
			resultHandler.waitFor();
		} catch (Exception e) {
			return Throwables.getStackTraceAsString(e);
		}
		return new String(byteArrayOutputStream.toByteArray());
	}
	
	public String exeShell(String scriptName, String... params) {
		return exeShellConetent("sh " + scriptPath + File.separator + scriptName + " " + Joiner.on(' ').join(params));
	}
}
