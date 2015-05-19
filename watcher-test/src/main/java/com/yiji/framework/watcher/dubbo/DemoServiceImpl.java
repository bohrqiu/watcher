package com.yiji.framework.watcher.dubbo;

/**
 * Created by daidai@yiji.com on 2015-03-06 15:05.
 */

/*
    实现对客户端不可见
*/

public class DemoServiceImpl implements DemoService {
	@Override
	public String sayHello(String name) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "Hello, " + name;
	}
}
