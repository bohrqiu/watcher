package com.yiji.framework.boot;

import com.yiji.framework.boot.BootstrapHelper;

/**
 * 
 * @author Bohr.Qiu <qiubo@yiji.com>
 * 
 */
public class WatherTomcatBootStrap {
	public static void main(final String[] args) {
		new BootstrapHelper(11112, false, "local").start();
	}
	
}
