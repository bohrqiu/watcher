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

import org.junit.Test;

import com.yiji.framework.watcher.metrics.shell.ShellExecutor;

/**
 * @author qiubo@yiji.com
 */
public class ExecTest {
	@Test
	public void testName() throws Exception {
		String line = "echo '你好'";
		ShellExecutor shellExecutor = new ShellExecutor();
		//System.out.println(shellExecutor.exeShellConetent(line));
		//System.out.println(shellExecutor.exeShell("test/test.sh"));
		
	}
	
	@Test
	public void test1() throws Exception {
		MonitorRequest request = new MonitorRequest();
		request.setAction("busyJavaThread");
		System.out.println(DefaultMonitorService.INSTANCE.monitor(request));

	}
}
