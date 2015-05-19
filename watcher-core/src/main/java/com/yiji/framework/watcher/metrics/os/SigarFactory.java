/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-25 22:09 创建
 *
 */
package com.yiji.framework.watcher.metrics.os;

import java.io.File;
import java.net.URL;

import org.hyperic.sigar.Sigar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;
import com.yiji.framework.watcher.Utils;

/**
 * @author qiubo@yiji.com
 */
public class SigarFactory {
	private static final Logger logger = LoggerFactory.getLogger(SigarFactory.class);
	private static final String sigarVersion = "1.6.4";
	
	private static final String sigarMacosxlib = "libsigar-universal64-macosx-" + sigarVersion + ".dylib";
	private static final String sigarLinuxAmd64lib = "libsigar-x86-linux-" + sigarVersion + ".so";
	private static final String sigarLinuxX86lib = "libsigar-x86-linux-" + sigarVersion + ".so";
	private static final String libPath = new File(System.getProperty("java.io.tmpdir") + File.separator + "watcher" + File.separator + "sigar")
		.getPath();
	private static final String[] libs = new String[] { libPath + File.separator + sigarMacosxlib, libPath + File.separator + sigarLinuxAmd64lib,
														libPath + File.separator + sigarLinuxX86lib };
	private static volatile boolean inited = false;
	
	static {
		initSigarLibPath();
	}
	
	private static void initSigarLibPath() {
		if (inited) {
			return;
		}
		synchronized (SigarFactory.class) {
			if (inited) {
				return;
			}
			logger.info("watcher sigar本地库安装路径:{}", libPath);
			try {
				//如果临时目录存在sigar，则设置路径
				if (!checkLocalLib()) {
					//不存在，需要解压jar包中的库文件到sigar目录
					URL libURL = Utils.getDefaultClassLoader().getResource(sigarLinuxX86lib);
					if (libURL == null || libURL.getFile() == null) {
						throw new RuntimeException("需要依赖org.fusesource:sigar:sigar-1.6.4-native,maven:\n" + "<dependency>\n"
													+ "\t<groupId>org.fusesource</groupId>\n" + "\t<artifactId>sigar</artifactId>\n" + "\t<version>"
													+ sigarVersion + "</version>\n" + "\t<classifier>native</classifier>\n" + "</dependency>");
					}
					String file = libURL.getFile().substring(0, libURL.getFile().indexOf(sigarLinuxX86lib) - 2);
					Utils.unzip(new URL(file).getFile(), libPath);
				}
				System.setProperty("org.hyperic.sigar.path", libPath);
			} catch (Throwable e) {
				logger.error("sigar加载失败", e);
				Throwables.propagate(e);
			} finally {
				inited = true;
			}
		}
	}
	
	private static boolean checkLocalLib() {
		for (String lib : libs) {
			if (!new File(lib).exists()) {
				return false;
			}
		}
		return true;
	}
	
	public static Sigar getSigar() {
		initSigarLibPath();
		return new Sigar();
	}
	
}
