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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.hyperic.sigar.Sigar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;

/**
 * @author qiubo@yiji.com
 */
public class SigarFactory {
	private static final Logger logger = LoggerFactory.getLogger(SigarFactory.class);
	private static final String sigarVersion = "1.6.4";
	
	private static final String sigarMacosxlib = "libsigar-universal64-macosx-" + sigarVersion + ".dylib";
	private static final String sigarLinuxAmd64lib = "libsigar-x86-linux-" + sigarVersion + ".so";
	private static final String sigarLinuxX86lib = "libsigar-x86-linux-" + sigarVersion + ".so";
	private static final String libPath = new File(System.getProperty("java.io.tmpdir") + File.separator + "sigar").getPath();
	private static final String[] libs = new String[] { libPath + File.separator + sigarMacosxlib, libPath + File.separator + sigarLinuxAmd64lib,
														libPath + File.separator + sigarLinuxX86lib };
	static {
		initSigarLibPath();
	}
	
	public static void initSigarLibPath() {
		logger.info("sigar本地库安装路径:{}", libPath);
		try {
			//如果临时目录存在sigar，则设置路径
			if (!checkLocalLib()) {
				//不存在，需要解压jar包中的库文件到sigar目录
				URL libURL = Thread.currentThread().getContextClassLoader().getResource(sigarLinuxX86lib);
				if (libURL == null || libURL.getFile() == null) {
					throw new RuntimeException("需要依赖org.fusesource:sigar:sigar-1.6.4-native,maven:\n" + "<dependency>\n"
												+ "\t<groupId>org.fusesource</groupId>\n" + "\t<artifactId>sigar</artifactId>\n" + "\t<version>"
												+ sigarVersion + "</version>\n" + "\t<classifier>native</classifier>\n" + "</dependency>");
				}
				String file = libURL.getFile().substring(0, libURL.getFile().indexOf(sigarLinuxX86lib) - 2);
				unzip(new URL(file).getFile(), libPath);
			}
			System.setProperty("org.hyperic.sigar.path", libPath);
		} catch (Exception e) {
			logger.error("sigar加载失败", e);
			Throwables.propagate(e);
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
	
	public static void unzip(String sourceFile, String toDir) throws Exception {
		ZipFile zipFile = new ZipFile(sourceFile);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			File entryDestination = new File(toDir, entry.getName());
			entryDestination.getParentFile().mkdirs();
			if (entry.isDirectory())
				entryDestination.mkdirs();
			else {
				InputStream in = null;
				OutputStream out = null;
				try {
					in = zipFile.getInputStream(entry);
					out = new FileOutputStream(entryDestination);
					ByteStreams.copy(in, out);
				} finally {
					Closeables.close(in, true);
					Closeables.close(out, true);
				}
				
			}
		}
		Closeables.close(zipFile, true);
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(SigarFactory.getSigar().getNetInfo().toMap());
	}
	
	public static Sigar getSigar() {
		return new Sigar();
	}
	
}
