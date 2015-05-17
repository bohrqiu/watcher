/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-15 14:14 创建
 *
 */
package com.yiji.framework.watcher.metrics.shell;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.yiji.framework.watcher.metrics.shell.ShellExecutor;

/**
 * @author qiubo@yiji.com
 */
public class ShellExecutorTest {
	@Test
	public void testExeShellConetent() throws Exception {
		String word = "hello world";
		String line = "echo '" + word + "'";
		ShellExecutor shellExecutor = new ShellExecutor();
		Assertions.assertThat(shellExecutor.exeShellConetent(line)).contains(word);
	}
	
	@Test
	public void testExeShell() throws Exception {
		String line = "test/test.sh";
		ShellExecutor shellExecutor = new ShellExecutor();
		Assertions.assertThat(shellExecutor.exeShell(line)).contains("hello 你好");
	}
}
