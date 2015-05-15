/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-29 11:21 创建
 *
 */
package com.yiji.framework.watcher;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;

/**
 * @author qiubo@yiji.com
 */
public class Utils {
	
	public static final String[] EMPTY_STRINGS = {};
	
	private static Long pid = null;
	
	public static String trim(String str) {
		if (str == null) {
			return null;
		} else {
			return str.trim();
		}
	}
	
	public static boolean isEmpty(String str) {
		return ((str == null) || (str.length() == 0));
	}
	
	public static Object getParam(Map<String, Object> params, String key, Object defaultValue) {
		if (params == null) {
			return null;
		}
		Object v = params.get(key);
		if (v == null) {
			v = defaultValue;
		}
		return v;
	}
	
	public static String[] split(String str, char separatorChar) {
		if (str == null) {
			return null;
		}
		int length = str.length();
		
		if (length == 0) {
			return EMPTY_STRINGS;
		}
		
		List list = new ArrayList();
		int i = 0;
		int start = 0;
		boolean match = false;
		
		while (i < length) {
			if (str.charAt(i) == separatorChar) {
				if (match) {
					list.add(str.substring(start, i));
					match = false;
				}
				start = ++i;
				continue;
			}
			match = true;
			i++;
		}
		if (match) {
			list.add(str.substring(start, i));
		}
		return (String[]) list.toArray(new String[list.size()]);
	}
	
	public static Long getPid() {
		if (pid == null) {
			final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
			final int index = jvmName.indexOf('@');
			
			if (index < 1) {
				return -1l;
			}
			try {
				pid = Long.parseLong(jvmName.substring(0, index));
				return pid;
			} catch (NumberFormatException e) {
				// ignore
				return -1l;
			}
		} else {
			return pid;
		}
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
	
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = Utils.class.getClassLoader();
			if (cl == null) {
				// getClassLoader() returning null indicates the bootstrap ClassLoader
				try {
					cl = ClassLoader.getSystemClassLoader();
				} catch (Throwable ex) {
					// Cannot access system ClassLoader - oh well, maybe the caller can live with null...
				}
			}
		}
		return cl;
	}
	
	public static Set<URL> findResourceFromClassPath(String path) throws Exception {
		Set<URL> result = new LinkedHashSet<>(8);
		Enumeration<URL> urlEnumeration = Utils.getDefaultClassLoader().getResources(path);
		if (urlEnumeration.hasMoreElements()) {
			URL url = urlEnumeration.nextElement();
			if (isJarResource(url)) {
				result.addAll(findPathMatchingJarResources(url));
			} else {
				result.addAll(findPathMatchingResources(url));
			}
		}
		return result;
	}
	
	private static Set<URL> findPathMatchingResources(URL rootUrl) throws IOException {
		Set<URL> result = new LinkedHashSet<>(8);
		doFindPathMatchingResources(rootUrl, result);
		return result;
	}
	
	private static void doFindPathMatchingResources(URL rootUrl, Set<URL> result) throws IOException {
		File rootDir = new File(rootUrl.getFile());
		File[] dirContents = rootDir.listFiles();
		if (dirContents == null) {
			return;
		}
		for (File content : dirContents) {
			if (content.isDirectory()) {
				URL dirUrl = new URL("file:" + content.getAbsolutePath() + File.separator);
				result.add(dirUrl);
				doFindPathMatchingResources(dirUrl, result);
			} else {
				result.add(new URL("file:" + content.getAbsolutePath()));
			}
		}
	}
	
	private static Set<URL> findPathMatchingJarResources(URL rootUrl) throws IOException {
		URLConnection con = rootUrl.openConnection();
		JarURLConnection jarCon = (JarURLConnection) con;
		JarFile jarFile = jarCon.getJarFile();
		JarEntry jarEntry = jarCon.getJarEntry();
		String rootEntryPath = (jarEntry != null ? jarEntry.getName() : "");
		
		if (!"".equals(rootEntryPath) && !rootEntryPath.endsWith("/")) {
			rootEntryPath = rootEntryPath + "/";
		}
		
		Set<URL> result = new LinkedHashSet<>(8);
		for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
			JarEntry entry = entries.nextElement();
			String entryPath = entry.getName();
			if (entryPath.startsWith(rootEntryPath)) {
				if (entryPath.startsWith("/")) {
					entryPath = entryPath.substring(1);
				}
				result.add(new URL(rootUrl, entryPath));
			}
		}
		return result;
	}
	
	public static boolean isJarResource(URL url) throws IOException {
		String protocol = url.getProtocol();
		return "jar".equals(protocol) || "zip".equals(protocol) || "vfszip".equals(protocol) || "wsjar".equals(protocol)
				|| "code-source".equals(protocol) && url.getPath().contains("!/");
	}
	
}
