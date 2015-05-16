/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-15 14:14 创建
 *
 */
package com.yiji.framework.watcher;

import java.net.URL;

import org.junit.Test;

import com.yiji.framework.watcher.metrics.shell.ShellExecutor;

/**
 * @author qiubo@yiji.com
 */
public class ExecTest {
	@Test
	public void testName() throws Exception {
		String line = "git status";
		ShellExecutor shellExecutor = new ShellExecutor();
		System.out.println(shellExecutor.exeShellConetent(line));
		//System.out.println(shellExecutor.exeShell("test/test.sh"));
		
	}
	
	@Test
	public void test1() throws Exception {
		MonitorRequest request = new MonitorRequest();
		request.setAction("busyJavaThread");
		System.out.println(DefaultMonitorService.INSTANCE.monitor(request));
		
	}
	
	@Test
	public void testName3() throws Exception {
		URL url = new URL("jar:file:/Users/bohr/.m2/repository/com/yiji/framework/yiji-watcher/1.4/yiji-watcher-1.4.jar!/watcher/script");
		URL newUrl = new URL(url, "/");
		System.out.println("ori:" + url);
		System.out.println("new:" + newUrl);
	}
}
