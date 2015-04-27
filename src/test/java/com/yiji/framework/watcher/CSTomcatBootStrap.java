package com.yiji.framework.watcher;

import com.yjf.common.test.TomcatBootstrapHelper;

/**
 * 
 * @author Bohr.Qiu <qzhanbo@yiji.com>
 * 
 */
public class CSTomcatBootStrap {
	public static void main(final String[] args) {
		new BootstrapHelper(11111,false,"local").start();
	}
	
}
